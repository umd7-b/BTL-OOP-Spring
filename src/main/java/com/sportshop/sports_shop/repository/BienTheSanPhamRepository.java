package com.sportshop.sports_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.BienTheSanPham;

@Repository
public interface BienTheSanPhamRepository extends JpaRepository<BienTheSanPham, Integer> {
    
    /**
     * Tìm biến thể theo mã sản phẩm
     */
    List<BienTheSanPham> findBySanPhamMaSp(Integer maSp);
    
    /**
     * Tìm biến thể còn hàng - FIX: dùng soLuongTon thay vì soLuong
     */
    List<BienTheSanPham> findBySoLuongTonGreaterThan(Integer soLuong);
}