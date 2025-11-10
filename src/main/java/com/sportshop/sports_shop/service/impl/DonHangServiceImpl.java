package com.sportshop.sports_shop.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportshop.sports_shop.dto.CreateOrderRequest;
import com.sportshop.sports_shop.dto.OrderItemDetailDto;
import com.sportshop.sports_shop.dto.OrderResponse;
import com.sportshop.sports_shop.dto.OrderSummaryDto;
import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.ChiTietDonHang;
import com.sportshop.sports_shop.model.DonHang;
import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.repository.BienTheSanPhamRepository;
import com.sportshop.sports_shop.repository.ChiTietDonHangRepository;
import com.sportshop.sports_shop.repository.DonHangRepository;
import com.sportshop.sports_shop.repository.SanPhamRepository;
import com.sportshop.sports_shop.service.DonHangService;
import com.sportshop.sports_shop.service.GioHangService;

@Service
public class DonHangServiceImpl implements DonHangService {
    
    
    @Autowired
    private DonHangRepository donHangRepository;
    
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;
    
    @Autowired
    private GioHangService gioHangService;
    
    @Autowired
    private BienTheSanPhamRepository bienTheSanPhamRepository;
    
    @Autowired
    private SanPhamRepository sanPhamRepository;
    
    /**
     * ✅ 1. Tạo đơn hàng (Đã có sẵn)
     */
    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        try {
            // ✅ 1. Kiểm tra FE đã gửi items chưa
            if (request.getItems() == null || request.getItems().isEmpty()) {
                throw new RuntimeException("Không có sản phẩm nào trong đơn hàng!");
            }

                List<String> errors = new ArrayList<>();
        for (var item : request.getItems()) {
            Optional<BienTheSanPham> bienTheOpt = bienTheSanPhamRepository.findById(item.getMaBienThe());
            
            if (!bienTheOpt.isPresent()) {
                errors.add("Sản phẩm (ID: " + item.getMaBienThe() + ") không tồn tại");
                continue;
            }
            
            BienTheSanPham bienThe = bienTheOpt.get();
            
            // Kiểm tra số lượng tồn kho
            if (bienThe.getSoLuongTon() < item.getSoLuong()) {
                String tenSp = bienThe.getSanPham() != null ? bienThe.getSanPham().getTenSp() : "Sản phẩm";
                errors.add(tenSp + " chỉ còn " + bienThe.getSoLuongTon() + " sản phẩm trong kho");
            }
            
            // Kiểm tra sản phẩm hết hàng
            if (bienThe.getSoLuongTon() <= 0) {
                String tenSp = bienThe.getSanPham() != null ? bienThe.getSanPham().getTenSp() : "Sản phẩm";
                errors.add(tenSp + " đã hết hàng");
            }
        }
        
        // Nếu có lỗi, trả về thông báo
        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join("; ", errors));
        }





            // ✅ 2. Tính tổng tiền từ FE gửi lên
            BigDecimal tongTien = request.getItems().stream()
                    .map(item -> BigDecimal.valueOf(item.getDonGia())
                    .multiply(BigDecimal.valueOf(item.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ✅ 3. Tạo đơn hàng
            DonHang donHang = new DonHang();
            donHang.setMaKh(request.getMaKhachHang());
            donHang.setNgayDat(LocalDateTime.now());
            donHang.setTongTien(tongTien);
            donHang.setTrangThai("CHO_XAC_NHAN");
            
            donHang.setTenNguoiNhan(request.getTenNguoiNhan());
            donHang.setSdtNguoiNhan(request.getSdtNguoiNhan());
            donHang.setDiaChiNguoiNhan(request.getDiaChiNguoiNhan());
            donHang.setPhuongThucThanhToan(request.getPhuongThucThanhToan());

            DonHang savedDonHang = donHangRepository.save(donHang);

            // ✅ 4. Lưu chi tiết đơn hàng từ FE
            // ✅ 4. Lưu chi tiết đơn hàng từ FE
    request.getItems().forEach(item -> {
  ChiTietDonHang ct = new ChiTietDonHang();
   ct.setMaDonHang(savedDonHang.getMaDonHang());
  ct.setMaBienThe(item.getMaBienThe());
   ct.setMaSp(item.getMaSp());
    ct.setSoLuong(item.getSoLuong());
    ct.setGia(BigDecimal.valueOf(item.getDonGia()));
    
    chiTietDonHangRepository.save(ct);

    // Trừ số lượng tồn kho
    BienTheSanPham bt = bienTheSanPhamRepository.findById(item.getMaBienThe())
     .orElseThrow(() -> new RuntimeException("Biến thể không tồn tại"));
    System.out.println("TRƯỚC KHI TRỪ: " + bt.getSoLuongTon());
    bt.setSoLuongTon(bt.getSoLuongTon() - item.getSoLuong());
    bienTheSanPhamRepository.save(bt);
    System.out.println("SAU KHI TRỪ: " + bt.getSoLuongTon());

    // ✅ Xoá khỏi giỏ hàng (ĐÃ SỬA LỖI)
    // Chúng ta bẫy lỗi ở đây để nó không làm hỏng transaction chính
    try {
    gioHangService.deleteItem(request.getMaKhachHang(), item.getMaBienThe());
    } catch (Exception e) {
    // Ghi log lỗi này lại nhưng KHÔNG ném exception ra ngoài
    System.err.println("Lỗi khi xoá sản phẩm " + item.getMaBienThe() + 
                    " khỏi giỏ hàng: " + e.getMessage());
    // Bạn có thể dùng logger chuyên nghiệp hơn ở đây
    }
    });

            // ✅ 5. Tạo response trả về FE
            OrderResponse res = new OrderResponse();
            res.setMaDonHang(savedDonHang.getMaDonHang());
            res.setMaKh(savedDonHang.getMaKh());
            res.setNgayDat(savedDonHang.getNgayDat());
            res.setTongTien(savedDonHang.getTongTien());
            res.setTrangThai(savedDonHang.getTrangThai());
            res.setTenNguoiNhan(savedDonHang.getTenNguoiNhan());
            res.setSdtNguoiNhan(savedDonHang.getSdtNguoiNhan());
            res.setDiaChiNguoiNhan(savedDonHang.getDiaChiNguoiNhan());
            res.setPhuongThucThanhToan(savedDonHang.getPhuongThucThanhToan());
            res.setMessage("Đặt hàng thành công!");

            return res;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

    /**
     * ✅ 2. Lấy đơn hàng theo ID
     */
    @Override
    public DonHang getOrderById(Integer maDonHang) {
        return donHangRepository.findById(maDonHang)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + maDonHang));
    }

    /**
     * ✅ 3. Lấy danh sách đơn hàng của khách hàng (Entity)
     */
    @Override
    public List<DonHang> getOrdersByCustomer(Long maKh) {
        return donHangRepository.findByMaKhOrderByNgayDatDesc(maKh);
    }

    /**
     * ✅ 4. Format trạng thái đơn hàng
     */
    @Override
    public String formatOrderStatus(String trangThai) {
        if (trangThai == null) return "Không xác định";
        
        switch (trangThai) {
            case "CHO_XAC_NHAN":
                return "Chờ xác nhận";
            case "DA_XAC_NHAN":
                return "Đã xác nhận";
            case "DANG_GIAO":
                return "Đang giao hàng";
            case "DA_GIAO":
                return "Đã giao hàng";
            case "HOAN_THANH":
                return "Hoàn thành";
            case "DA_HUY":
                return "Đã hủy";
            default:
                return trangThai;
        }
    }

    /**
     * ✅ 5. Lấy danh sách đơn hàng của khách hàng (DTO)
     */
    public List<OrderResponse> getDonHangByKhachHang(Long maKh) {
        try {
            List<DonHang> donHangs = donHangRepository.findByMaKhOrderByNgayDatDesc(maKh);
            
            return donHangs.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * ✅ 6. Lấy chi tiết một đơn hàng (DTO)
     */
    public OrderResponse getDonHangById(Integer maDonHang) {
        DonHang donHang = donHangRepository.findById(maDonHang)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + maDonHang));
        
        return convertToOrderResponse(donHang);
    }

    /**
     * ✅ 7. Lấy danh sách sản phẩm trong đơn hàng
     */
    public List<OrderItemDetailDto> getChiTietDonHang(Integer maDonHang) {
        try {
            List<ChiTietDonHang> chiTietList = chiTietDonHangRepository.findByMaDonHang(maDonHang);
            
            List<OrderItemDetailDto> result = new ArrayList<>();
            
            for (ChiTietDonHang ct : chiTietList) {
                OrderItemDetailDto dto = new OrderItemDetailDto();
                dto.setMaCtDonHang(ct.getMaCtDonHang());
                dto.setMaBienThe(ct.getMaBienThe());
                dto.setSoLuong(ct.getSoLuong());
                dto.setGia(ct.getGia());
                dto.setThanhTien(ct.getGia().multiply(BigDecimal.valueOf(ct.getSoLuong())));
                
                // Lấy thông tin biến thể
                Optional<BienTheSanPham> bienTheOpt = bienTheSanPhamRepository.findById(ct.getMaBienThe());
                if (bienTheOpt.isPresent()) {
                    BienTheSanPham bienThe = bienTheOpt.get();
                    
                    // Tạo text phân loại giống GioHangService
                    String phanLoai = "";
                    if (bienThe.getMauSac() != null && !bienThe.getMauSac().isEmpty()) {
                        phanLoai += bienThe.getMauSac();
                    }
                    if (bienThe.getKichThuoc() != null && !bienThe.getKichThuoc().isEmpty()) {
                        if (!phanLoai.isEmpty()) phanLoai += " - ";
                        phanLoai += bienThe.getKichThuoc();
                    }
                    dto.setTenBienThe(phanLoai);
                    dto.setMaSp(bienThe.getSanPham().getMaSp());
                    
                    // Lấy thông tin sản phẩm
                    if (bienThe.getSanPham() != null) {
                        SanPham sanPham = bienThe.getSanPham();
                        dto.setTenSanPham(sanPham.getTenSp());
                        
                        // Lấy hình ảnh đầu tiên giống GioHangService
                        if (sanPham.getDanhSachAnh() != null && !sanPham.getDanhSachAnh().isEmpty()) {
                            dto.setHinhAnh("/uploads/products/" + sanPham.getDanhSachAnh().get(0).getLinkAnh());
                        } else {
                            dto.setHinhAnh("/client/images/no-image.png");
                        }
                    } else {
                        // Fallback: Tìm sản phẩm qua maSp
                        Optional<SanPham> sanPhamOpt = sanPhamRepository.findById(bienThe.getSanPham().getMaSp().longValue());
                        if (sanPhamOpt.isPresent()) {
                            SanPham sanPham = sanPhamOpt.get();
                            dto.setTenSanPham(sanPham.getTenSp());
                            
                            if (sanPham.getDanhSachAnh() != null && !sanPham.getDanhSachAnh().isEmpty()) {
                                dto.setHinhAnh("/uploads/products/" + sanPham.getDanhSachAnh().get(0).getLinkAnh());
                            } else {
                                dto.setHinhAnh("/client/images/no-image.png");
                            }
                        }
                    }
                }
                
                result.add(dto);
            }
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * ✅ 8. Kiểm tra đơn hàng có thuộc về khách hàng không
     */
    public boolean kiemTraDonHangCuaKhachHang(Integer maDonHang, Long maKh) {
        Optional<DonHang> donHangOpt = donHangRepository.findById(maDonHang);
        
        if (donHangOpt.isPresent()) {
            return donHangOpt.get().getMaKh().equals(maKh);
        }
        
        return false;
    }

    /**
     * ✅ 9. Hủy đơn hàng
     */
    @Transactional
    public OrderResponse huyDonHang(Integer maDonHang) {
        DonHang donHang = donHangRepository.findById(maDonHang)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        
        OrderResponse response = new OrderResponse();
        response.setMaDonHang(maDonHang);
        
        // Chỉ cho phép hủy đơn hàng ở trạng thái CHO_XAC_NHAN
        if (!"CHO_XAC_NHAN".equals(donHang.getTrangThai())) {
            response.setMessage("Không thể hủy đơn hàng ở trạng thái: " + formatOrderStatus(donHang.getTrangThai()));
            response.setTrangThai(donHang.getTrangThai());
            return response;
        }
        
        // Cập nhật trạng thái
        donHang.setTrangThai("DA_HUY");
        donHangRepository.save(donHang);
        
        // Hoàn lại số lượng sản phẩm
        List<ChiTietDonHang> chiTietList = chiTietDonHangRepository.findByMaDonHang(maDonHang);
        for (ChiTietDonHang ct : chiTietList) {
            Optional<BienTheSanPham> bienTheOpt = bienTheSanPhamRepository.findById(ct.getMaBienThe());
            if (bienTheOpt.isPresent()) {
                BienTheSanPham bienThe = bienTheOpt.get();
                bienThe.setSoLuongTon(bienThe.getSoLuongTon()+ ct.getSoLuong());
                bienTheSanPhamRepository.save(bienThe);
            }
        }
        
        response.setTrangThai("DA_HUY");
        response.setMessage("Hủy đơn hàng thành công");
        
        return response;
    }

    /**
     * ✅ 10. Convert DonHang entity sang OrderResponse DTO
     */
    private OrderResponse convertToOrderResponse(DonHang donHang) {
        OrderResponse response = new OrderResponse();
        response.setMaDonHang(donHang.getMaDonHang());
        response.setMaKh(donHang.getMaKh());
        response.setNgayDat(donHang.getNgayDat());
        response.setTongTien(donHang.getTongTien());
        response.setTrangThai(donHang.getTrangThai());
        response.setTenNguoiNhan(donHang.getTenNguoiNhan());
        response.setSdtNguoiNhan(donHang.getSdtNguoiNhan());
        response.setDiaChiNguoiNhan(donHang.getDiaChiNguoiNhan());
        response.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        
        return response;
    }
    @Override
public Map<String, Object> getOrderStatistics() {
    List<DonHang> allOrders = donHangRepository.findAll();
    
    Map<String, Object> stats = new HashMap<>();
    stats.put("total", allOrders.size());
    stats.put("choXacNhan", allOrders.stream().filter(o -> "CHO_XAC_NHAN".equals(o.getTrangThai())).count());
    stats.put("daXacNhan", allOrders.stream().filter(o -> "DA_XAC_NHAN".equals(o.getTrangThai())).count());
    stats.put("dangGiao", allOrders.stream().filter(o -> "DANG_GIAO".equals(o.getTrangThai())).count());
    stats.put("daGiao", allOrders.stream().filter(o -> "DA_GIAO".equals(o.getTrangThai())).count());
    stats.put("daHuy", allOrders.stream().filter(o -> "DA_HUY".equals(o.getTrangThai())).count());
    
    return stats;
}

/**
 * ✅ 13. Cập nhật trạng thái đơn hàng (Admin)
 */
@Override
@Transactional
public OrderResponse updateOrderStatus(Integer maDonHang, String newStatus) {
    DonHang donHang = donHangRepository.findById(maDonHang)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + maDonHang));
    
    OrderResponse response = new OrderResponse();
    response.setMaDonHang(maDonHang);
    
    // Kiểm tra trạng thái hợp lệ
    List<String> validStatuses = List.of("CHO_XAC_NHAN", "DA_XAC_NHAN", "DANG_GIAO", "DA_GIAO", "DA_HUY");
    if (!validStatuses.contains(newStatus)) {
        response.setMessage("Trạng thái không hợp lệ");
        return response;
    }
    
    String oldStatus = donHang.getTrangThai();
    donHang.setTrangThai(newStatus);
    donHangRepository.save(donHang);
    
    // Nếu chuyển từ trạng thái khác sang DA_HUY, hoàn lại số lượng
    if (!"DA_HUY".equals(oldStatus) && "DA_HUY".equals(newStatus)) {
        List<ChiTietDonHang> chiTietList = chiTietDonHangRepository.findByMaDonHang(maDonHang);
        for (ChiTietDonHang ct : chiTietList) {
            Optional<BienTheSanPham> bienTheOpt = bienTheSanPhamRepository.findById(ct.getMaBienThe());
            if (bienTheOpt.isPresent()) {
                BienTheSanPham bienThe = bienTheOpt.get();
                bienThe.setSoLuongTon(bienThe.getSoLuongTon() + ct.getSoLuong());
                bienTheSanPhamRepository.save(bienThe);
            }
        }
    }
    
    response.setTrangThai(newStatus);
    response.setMessage("Cập nhật trạng thái thành công");
    
    return response;
}

/**
 * ✅ 14. Xóa đơn hàng (Admin)
 */
@Override
@Transactional
public void deleteOrder(Integer maDonHang) {
    DonHang donHang = donHangRepository.findById(maDonHang)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng #" + maDonHang));
    
    // Xóa chi tiết đơn hàng trước
    chiTietDonHangRepository.deleteByMaDonHang(maDonHang);
    
    // Xóa đơn hàng
    donHangRepository.delete(donHang);
}

   
    private OrderSummaryDto convertToSummary(DonHang donHang) {
    OrderSummaryDto dto = new OrderSummaryDto();
    dto.setMaDonHang(donHang.getMaDonHang());
    dto.setMaKh(donHang.getMaKh());
    dto.setNgayDat(donHang.getNgayDat());
    dto.setTongTien(donHang.getTongTien());
    dto.setTrangThai(donHang.getTrangThai());
    dto.setTenNguoiNhan(donHang.getTenNguoiNhan());
    return dto;
    }
    @Override
    public List<OrderSummaryDto> getAllOrders() {
        List<DonHang> list = donHangRepository.findAllByOrderByNgayDatDesc();

        return list.stream()
                .map(this::convertToSummary)
                .toList();
    }

}