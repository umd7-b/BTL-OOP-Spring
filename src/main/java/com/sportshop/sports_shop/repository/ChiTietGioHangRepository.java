 package com.sportshop.sports_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
 import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sportshop.sports_shop.model.ChiTietGioHang;



@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Integer> {

    ChiTietGioHang findByGioHangMaGioHangAndBienTheMaBienThe(Integer maGioHang, Integer maBienThe);

    List<ChiTietGioHang> findByGioHangMaGioHang(Integer maGioHang);
     @Modifying

    @Transactional

    void deleteByGioHangMaGioHangAndBienTheMaBienThe(Integer maGioHang, Integer maBienThe);

    void deleteByGioHangMaGioHang(Integer maGioHang);

    List<ChiTietGioHang> findByGioHangMaGioHangAndDaChon(Integer maGioHang, Boolean daChon);

    @Query("SELECT SUM(ct.gia * ct.soLuong) FROM ChiTietGioHang ct WHERE ct.gioHang.maGioHang = :id")
    Double sumTotal(Integer id);

    @Query("SELECT SUM(ct.gia * ct.soLuong) FROM ChiTietGioHang ct WHERE ct.gioHang.maGioHang = :id AND ct.daChon = true")
    Double sumSelected(Integer id);
    
   
    
}



