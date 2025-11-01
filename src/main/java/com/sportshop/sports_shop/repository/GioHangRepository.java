package com.sportshop.sports_shop.repository;

import com.sportshop.sports_shop.model.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Long> {
    
    // Tự động tạo câu lệnh: "SELECT * FROM gio_hang WHERE ma_khach_hang = ?"
    Optional<GioHang> findByKhachHang_MaKhachHang(Long maKhachHang);

    // Tự động tạo: "SELECT * FROM gio_hang WHERE khachHang_tenDangNhap = ?"
    Optional<GioHang> findByKhachHang_TenDangNhap(String tenDangNhap);
}