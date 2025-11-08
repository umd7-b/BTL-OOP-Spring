package com.sportshop.sports_shop.service;

import java.util.List;

import com.sportshop.sports_shop.model.ChiTietGioHang;

public interface GioHangService {

    // ✅ HÀM GỐC – chỉ 1 lần duy nhất
    ChiTietGioHang addToCart(Long maKhachHang, Integer maBienThe, Integer soLuong, Double gia, boolean merge);

    List<ChiTietGioHang> getCart(Long maKhachHang);

    ChiTietGioHang updateQuantity(Integer maCtGioHang, Integer soLuong);

    void deleteItem(Long maKhachHang, Integer maBienThe);

    void clearCart(Long maKhachHang);

    ChiTietGioHang toggleSelect(Integer maCtGioHang);

    void toggleSelectAll(Long maKhachHang, Integer chon);

    List<ChiTietGioHang> getSelectedItems(Long maKhachHang);

    Double getCartTotal(Long maKhachHang);

    Double getSelectedTotal(Long maKhachHang);


    // ✅ Overload 4 tham số – Controller sẽ gọi cái này
    default ChiTietGioHang addToCart(Long maKhachHang, Integer maBienThe, Integer soLuong, Double gia) {
        return addToCart(maKhachHang, maBienThe, soLuong, gia, true);
        
    }
}