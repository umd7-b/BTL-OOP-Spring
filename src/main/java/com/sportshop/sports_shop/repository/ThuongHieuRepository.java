package com.sportshop.sports_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.ThuongHieu;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> {
    // Spring Data JPA sẽ tự động tạo các hàm cơ bản
    
    // (Lưu ý: Tôi cũng dùng "Integer" làm kiểu ID để khớp với 
    // các model liên quan đến SanPham)
}