package com.sportshop.sports_shop.service;

// Import Models
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportshop.sports_shop.model.ChiTietDonHang;
import com.sportshop.sports_shop.model.ChiTietGioHang;
import com.sportshop.sports_shop.model.DonHang;
import com.sportshop.sports_shop.model.GioHang;
import com.sportshop.sports_shop.model.KhachHang;
import com.sportshop.sports_shop.model.SanPham;
 import com.sportshop.sports_shop.repository.DonHangRepository;
import com.sportshop.sports_shop.repository.GioHangRepository;
import com.sportshop.sports_shop.repository.KhachHangRepository;

import jakarta.persistence.EntityNotFoundException;
@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;
    
    // (Lưu ý: Chúng ta không cần ChiTietGioHangRepository ở đây
    // vì chúng ta sẽ lấy ChiTiet qua đối tượng GioHang)

    /**
     * Tạo một đơn hàng mới cho khách hàng từ giỏ hàng hiện tại của họ.
     * @param tenDangNhap Tên đăng nhập của khách hàng
     * @return Đơn hàng vừa được tạo
     * @throws EntityNotFoundException nếu không tìm thấy khách hàng hoặc giỏ hàng
     * @throws IllegalStateException nếu giỏ hàng trống
     */
    @Transactional // Rất quan trọng: Đảm bảo tất cả các bước (tạo đơn, xóa giỏ)
                   // cùng thành công, hoặc cùng thất bại (rollback)
    public DonHang createOrderFromCart(String tenDangNhap) {
        
   
        KhachHang khachHang = khachHangRepository.findByTenDangNhap(tenDangNhap);
                        if (khachHang == null) {
        throw new EntityNotFoundException("Không tìm thấy khách hàng: " + tenDangNhap);
        }


        // 2. Tìm giỏ hàng của khách
        GioHang gioHang = gioHangRepository.findByKhachHang_MaKhachHang(khachHang.getMaKhachHang())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giỏ hàng cho khách hàng."));

        Set<ChiTietGioHang> chiTietGioHangs = gioHang.getChiTietGioHangs();

        // 3. Kiểm tra xem giỏ hàng có trống không
        if (chiTietGioHangs.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng trống, không thể đặt hàng.");
        }

        // 4. Tạo đối tượng DonHang mới
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setNgayDatHang(LocalDateTime.now());
        donHang.setTrangThai("Chờ xử lý"); // Trạng thái mặc định
        donHang.setDiaChiGiaoHang(khachHang.getDiaChi()); // Lấy địa chỉ mặc định từ khách hàng
        donHang.setSoDienThoai(khachHang.getSoDienThoai()); // Lấy SĐT mặc định

        BigDecimal tongTien = BigDecimal.ZERO;

        // 5. Chuyển đổi ChiTietGioHang thành ChiTietDonHang
        for (ChiTietGioHang ctGioHang : chiTietGioHangs) {
            ChiTietDonHang ctDonHang = new ChiTietDonHang();
            
            SanPham sanPham = ctGioHang.getSanPham();
            int soLuong = ctGioHang.getSoLuong();
            BigDecimal donGia = sanPham.getGiaBan(); // Lấy giá HIỆN TẠI của sản phẩm
            
            ctDonHang.setSanPham(sanPham);
            ctDonHang.setSoLuong(soLuong);
            ctDonHang.setDonGia(donGia); // Lưu lại giá tại thời điểm mua

            // Tính tổng tiền cho chi tiết này
            BigDecimal subTotal = donGia.multiply(BigDecimal.valueOf(soLuong));
            tongTien = tongTien.add(subTotal);

            // Dùng helper method để liên kết 2 chiều
            donHang.addChiTiet(ctDonHang); 
        }

        donHang.setTongTien(tongTien);

        // 6. Lưu đơn hàng (và các chi tiết đơn hàng) vào DB
        // Nhờ CascadeType.ALL, các ChiTietDonHang sẽ tự động được lưu
        DonHang savedDonHang = donHangRepository.save(donHang);

        // 7. XÓA RỖNG giỏ hàng
        // (Chúng ta không xóa ChiTietGioHang bằng repo, mà dùng tính năng
        // orphanRemoval = true đã cấu hình trong GioHang)
        gioHang.getChiTietGioHangs().clear();
        gioHang.setNgayCapNhat(LocalDateTime.now());
        gioHangRepository.save(gioHang);

        return savedDonHang;
    }
}