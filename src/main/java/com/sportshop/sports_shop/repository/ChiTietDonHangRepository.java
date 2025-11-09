package com.sportshop.sports_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.ChiTietDonHang;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {
    
    // Tìm chi tiết đơn hàng theo mã đơn hàng
    List<ChiTietDonHang> findByMaDonHang(Integer maDonHang);
    
    // Xóa chi tiết đơn hàng theo mã đơn hàng
    void deleteByMaDonHang(Integer maDonHang);
}


