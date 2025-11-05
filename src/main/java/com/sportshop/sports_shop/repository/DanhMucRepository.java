package com.sportshop.sports_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.DanhMuc;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    DanhMuc findByTenDanhMuc(String ten);
    boolean existsByTenDanhMuc(String ten);
}
