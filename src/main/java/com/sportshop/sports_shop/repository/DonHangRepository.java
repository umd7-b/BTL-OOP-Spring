package com.sportshop.sports_shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportshop.sports_shop.model.DonHang;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    
    // Tìm đơn hàng theo mã khách hàng, sắp xếp theo ngày đặt giảm dần
    List<DonHang> findByMaKhOrderByNgayDatDesc(Long maKh);
    
    // Tìm đơn hàng theo mã khách hàng và trạng thái
    List<DonHang> findByMaKhAndTrangThai(Long maKh, String trangThai);
    
    // Đếm số đơn hàng của khách hàng
    long countByMaKh(Long maKh);
    
    // Tìm đơn hàng theo ID và mã khách hàng (để kiểm tra quyền sở hữu)
    Optional<DonHang> findByMaDonHangAndMaKh(Integer maDonHang, Long maKh);
    
    // Tìm đơn hàng theo nhiều trạng thái
    @Query("SELECT d FROM DonHang d WHERE d.maKh = :maKh AND d.trangThai IN :trangThais ORDER BY d.ngayDat DESC")
    List<DonHang> findByMaKhAndTrangThaiIn(@Param("maKh") Long maKh, @Param("trangThais") List<String> trangThais);
    List<DonHang> findAllByOrderByNgayDatDesc();
}

