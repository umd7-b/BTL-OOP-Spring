package com.sportshop.sports_shop.repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Query("SELECT COALESCE(SUM(d.tongTien), 0) " +
           "FROM DonHang d " +
           "WHERE d.trangThai = :status")
    BigDecimal sumRevenueByStatus(@Param("status") String status);
     @Query("""
        SELECT DATE(d.ngayDat), COALESCE(SUM(d.tongTien), 0)
        FROM DonHang d
        WHERE d.trangThai = 'DA_GIAO'
          AND d.ngayDat BETWEEN :start AND :end
        GROUP BY DATE(d.ngayDat)
        ORDER BY DATE(d.ngayDat)
    """)
    List<Object[]> getRevenueByDateRange(LocalDateTime start, LocalDateTime end);


    // ==========================
    // SỐ LƯỢNG ĐƠN THEO NGÀY
    // ==========================
    @Query("""
        SELECT DATE(d.ngayDat), COUNT(d)
        FROM DonHang d
        WHERE d.trangThai = 'DA_GIAO'
          AND d.ngayDat BETWEEN :start AND :end
        GROUP BY DATE(d.ngayDat)
        ORDER BY DATE(d.ngayDat)
    """)
    List<Object[]> getOrderCountByDate(LocalDateTime start, LocalDateTime end);


    // ==========================
    // DOANH THU THEO THÁNG
    // ==========================
    @Query("""
        SELECT MONTH(d.ngayDat), SUM(d.tongTien)
        FROM DonHang d
        WHERE d.trangThai = 'DA_GIAO' 
          AND YEAR(d.ngayDat) = :year
        GROUP BY MONTH(d.ngayDat)
        ORDER BY MONTH(d.ngayDat)
    """)
    List<Object[]> getMonthlyRevenue(int year);


    // ==========================
    // SỐ ĐƠN THEO THÁNG
    // ==========================
    @Query("""
        SELECT MONTH(d.ngayDat), COUNT(d)
        FROM DonHang d
        WHERE d.trangThai = 'DA_GIAO' 
          AND YEAR(d.ngayDat) = :year
        GROUP BY MONTH(d.ngayDat)
        ORDER BY MONTH(d.ngayDat)
    """)
    List<Object[]> getMonthlyOrderCount(int year);


    // ==========================
    // DOANH THU THEO NĂM (5 năm)
    // ==========================
    @Query("""
        SELECT YEAR(d.ngayDat), SUM(d.tongTien)
        FROM DonHang d
        WHERE d.trangThai = 'DA_GIAO'
          AND YEAR(d.ngayDat) BETWEEN :startYear AND :endYear
        GROUP BY YEAR(d.ngayDat)
        ORDER BY YEAR(d.ngayDat)
    """)
    List<Object[]> getRevenueLast5Years(int startYear, int endYear);


    // ==========================
    // SỐ ĐƠN THEO NĂM (5 năm)
    // ==========================
    @Query("""
        SELECT YEAR(d.ngayDat), COUNT(d)
        FROM DonHang d
        WHERE d.trangThai = 'DA_GIAO'
          AND YEAR(d.ngayDat) BETWEEN :startYear AND :endYear
        GROUP BY YEAR(d.ngayDat)
        ORDER BY YEAR(d.ngayDat)
    """)
    List<Object[]> getOrderCountLast5Years(int startYear, int endYear);
    @Query("""
    SELECT COUNT(d)
    FROM DonHang d
    WHERE d.maKh = :maKh
      AND d.trangThai IN ('CHO_XAC_NHAN', 'DANG_XAC_NHAN', 'DANG_GIAO','DA_GIAO','DA_HUY')
""")
long countPendingOrdersByKhachHang(@Param("maKh") Long maKh);


   

  
  



}

