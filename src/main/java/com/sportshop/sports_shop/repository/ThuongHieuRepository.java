package com.sportshop.sports_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sportshop.sports_shop.model.ThuongHieu;

@Repository
// Bạn đã đổi sang Long, rất tốt!
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> { 

    // BẠN THIẾU DẤU ; Ở ĐÂY
    ThuongHieu findByTenThuongHieu(String ten); 

    // BẠN THIẾU DẤU ; Ở ĐÂY
    boolean existsByTenThuongHieu(String ten);
}