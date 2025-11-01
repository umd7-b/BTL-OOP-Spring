package com.sportshop.sports_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.KhachHang;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    // Tìm khách hàng theo tên đăng nhập
    KhachHang findByTenDangNhap(String tenDangNhap);

    // Tìm khách hàng theo email (nếu cần)
    KhachHang findByEmail(String email);
}
