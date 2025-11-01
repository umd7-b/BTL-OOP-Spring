package com.sportshop.sports_shop.repository;

import com.sportshop.sports_shop.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Kế thừa JpaRepository với Entity là SanPham và kiểu dữ liệu của ID là Integer
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các hàm CRUD cơ bản (save, findById, findAll,...)
}