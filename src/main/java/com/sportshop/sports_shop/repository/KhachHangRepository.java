package com.sportshop.sports_shop.repository;

import com.sportshop.sports_shop.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    KhachHang findByEmail(String email);

    boolean existsByEmail(String email);

    KhachHang findByTenDangNhap(String tenDangNhap);

    boolean existsByTenDangNhap(String tenDangNhap);
}
