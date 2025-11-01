package com.sportshop.sports_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.DanhMuc;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    // Spring Data JPA sẽ tự động tạo các hàm cơ bản như 
    // findAll(), findById(), save()...
    
    // (Lưu ý: Tôi đang dùng "Integer" làm kiểu ID, 
    // vì file SanPham.java của bạn dùng "Integer maSp").
}