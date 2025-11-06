package com.sportshop.sports_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sportshop.sports_shop.model.ThuongHieu;

@Repository

public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> { 

    ThuongHieu findByTenThuongHieu(String ten); 

   
    boolean existsByTenThuongHieu(String ten);
}