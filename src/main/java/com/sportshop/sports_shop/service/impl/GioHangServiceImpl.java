package com.sportshop.sports_shop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.ChiTietGioHang;
import com.sportshop.sports_shop.model.GioHang;
import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.repository.BienTheSanPhamRepository;
import com.sportshop.sports_shop.repository.ChiTietGioHangRepository;
import com.sportshop.sports_shop.repository.GioHangRepository;
import com.sportshop.sports_shop.service.GioHangService;
import com.sportshop.sports_shop.dto.CartItemDTO;
import java.util.stream.Collectors;
@Service
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    private GioHangRepository gioHangRepo;

    @Autowired
    private ChiTietGioHangRepository ctRepo;

    @Autowired
    private BienTheSanPhamRepository bienTheRepo;

    // ✅ Lấy hoặc tạo giỏ hàng
    private GioHang getOrCreateCart(Integer maKhachHang) {
        return gioHangRepo.findByMaKh(maKhachHang.longValue())
                .orElseGet(() -> {
                    GioHang gh = new GioHang();
                    gh.setMaKh(maKhachHang.longValue());  // chỉ set mã khách
                    return gioHangRepo.save(gh);          // DB tự sinh maGioHang
                });
    }

    // ✅ 1. Thêm vào giỏ
    @Override
    public ChiTietGioHang addToCart(Long maKhachHang, Integer maBienThe, Integer soLuong, Double gia, boolean merge) {

        GioHang cart = getOrCreateCart(maKhachHang.intValue());

        BienTheSanPham bt = bienTheRepo.findById(maBienThe)
                .orElseThrow(() -> new RuntimeException("Biến thể không tồn tại"));

        // ⭐ Kiểm tra BienTheSanPham có SanPham không
        if (bt.getSanPham() == null) {
            throw new RuntimeException("Biến thể không liên kết với sản phẩm");
        }

        if (merge) {
            ChiTietGioHang exist =
                    ctRepo.findByGioHangMaGioHangAndBienTheMaBienThe(cart.getMaGioHang(), maBienThe);

            if (exist != null) {
                exist.setSoLuong(exist.getSoLuong() + soLuong);
                return ctRepo.save(exist);
            }
        }

        // ✅ Tạo mới - PHẢI SET ma_sp
        ChiTietGioHang ct = new ChiTietGioHang();
        ct.setGioHang(cart);
        ct.setBienThe(bt);
        ct.setMaSp(bt.getSanPham().getMaSp());  // ⭐ THÊM DÒNG NÀY - LẤY MÃ SẢN PHẨM TỪ BIẾN THỂ
        ct.setSoLuong(soLuong);
        ct.setGia(BigDecimal.valueOf(gia));
        ct.setDaChon(false);

        return ctRepo.save(ct);
    }

    // ✅ 2. Lấy toàn bộ giỏ hàng
    @Override
    public List<ChiTietGioHang> getCart(Long maKhachHang) {
        GioHang cart = getOrCreateCart(maKhachHang.intValue());
        return ctRepo.findByGioHangMaGioHang(cart.getMaGioHang());
    }

    // ✅ 3. Cập nhật số lượng
    @Override
    public ChiTietGioHang updateQuantity(Integer maCtGioHang, Integer soLuong) {
        ChiTietGioHang ct = ctRepo.findById(maCtGioHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục"));
        ct.setSoLuong(soLuong);
        return ctRepo.save(ct);
    }

    // ✅ 4. Xóa 1 sản phẩm khỏi giỏ
    @Override
    public void deleteItem(Long maKhachHang, Integer maBienThe) {
        GioHang cart = getOrCreateCart(maKhachHang.intValue());
        ctRepo.deleteByGioHangMaGioHangAndBienTheMaBienThe(
                cart.getMaGioHang(), maBienThe
        );
    }

    // ✅ 5. Xóa toàn bộ giỏ hàng
    @Override
    public void clearCart(Long maKhachHang) {
        GioHang cart = getOrCreateCart(maKhachHang.intValue());
        ctRepo.deleteByGioHangMaGioHang(cart.getMaGioHang());
    }

    // ✅ 6. Chọn / bỏ chọn 1 item
    @Override
    public ChiTietGioHang toggleSelect(Integer maCtGioHang) {
        ChiTietGioHang ct = ctRepo.findById(maCtGioHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục"));
        ct.setDaChon(!ct.getDaChon());
        return ctRepo.save(ct);
    }

    // ✅ 7. Chọn / bỏ chọn tất cả
    @Override
    public void toggleSelectAll(Long maKhachHang, Integer chon) {
        GioHang gh = getOrCreateCart(maKhachHang.intValue());
        List<ChiTietGioHang> list = ctRepo.findByGioHangMaGioHang(gh.getMaGioHang());
        boolean state = (chon == 1);
        list.forEach(ct -> ct.setDaChon(state));
        ctRepo.saveAll(list);
    }

    // ✅ 8. Lấy danh sách item đã tích chọn
    @Override
    public List<ChiTietGioHang> getSelectedItems(Long maKhachHang) {
        GioHang cart = getOrCreateCart(maKhachHang.intValue());
        return ctRepo.findByGioHangMaGioHangAndDaChon(cart.getMaGioHang(), true);
    }

    // ✅ 9. Tổng tiền giỏ hàng
    @Override
    public Double getCartTotal(Long maKhachHang) {
        GioHang cart = getOrCreateCart(maKhachHang.intValue());
        return ctRepo.sumTotal(cart.getMaGioHang());
    }

    // ✅ 10. Tổng tiền đã chọn
    @Override
    public Double getSelectedTotal(Long maKhachHang) {
        GioHang cart = getOrCreateCart(maKhachHang.intValue());
        return ctRepo.sumSelected(cart.getMaGioHang());
    }
    public List<CartItemDTO> getCartDTO(Long maKhachHang) {
    try {
        GioHang cart = getOrCreateCart(maKhachHang.intValue());
        List<ChiTietGioHang> items = ctRepo.findByGioHangMaGioHang(cart.getMaGioHang());
        
        return items.stream()
            .filter(item -> item.getBienThe() != null && item.getBienThe().getSanPham() != null)
            .map(item -> {
                BienTheSanPham bt = item.getBienThe();
                SanPham sp = bt.getSanPham();
                
                CartItemDTO dto = new CartItemDTO();
                dto.setMaCtGioHang(item.getMaCtGioHang());
                dto.setMaBienThe(bt.getMaBienThe());
                dto.setMaSp(sp.getMaSp().intValue());
                dto.setTenSp(sp.getTenSp());
                
                // Lấy hình ảnh đầu tiên
                if (sp.getDanhSachAnh() != null && !sp.getDanhSachAnh().isEmpty()) {
                    dto.setHinhAnh("/uploads/products/" + sp.getDanhSachAnh().get(0).getLinkAnh());
                } else {
                    dto.setHinhAnh("/client/images/no-image.png");
                }
                
                // Tạo text phân loại
                String phanLoai = "";
                if (bt.getMauSac() != null) phanLoai += bt.getMauSac();
                if (bt.getKichThuoc() != null) {
                    if (!phanLoai.isEmpty()) phanLoai += " - ";
                    phanLoai += bt.getKichThuoc();
                }
                dto.setPhanLoaiText(phanLoai);
                
                dto.setSoLuong(item.getSoLuong());
                dto.setGia(item.getGia().doubleValue());
                dto.setDaChon(item.getDaChon());
                
                return dto;
            })
            .collect(Collectors.toList());
            
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
}