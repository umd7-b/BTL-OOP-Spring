package com.sportshop.sports_shop.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.SanPham;


@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {


    List<SanPham> findByTenSpContainingIgnoreCase(String keyword);


    List<SanPham> findByThuongHieuMaThuongHieu(Integer maThuongHieu);


    List<SanPham> findByDanhMucMaDanhMuc(Integer maDanhMuc);


    List<SanPham> findByMonTheThaoMaMonTheThao(Integer maMonTheThao);
    @Query("""
    SELECT sp FROM SanPham sp
    LEFT JOIN FETCH sp.bienThe
    WHERE sp.maSp = :id
    """)
    Optional<SanPham> findByIdWithRelations(@Param("id") Long id);
}


