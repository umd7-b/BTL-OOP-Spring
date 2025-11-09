package com.sportshop.sports_shop.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "chi_tiet_don_hang")
@Data
public class ChiTietDonHang {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ct_don_hang")
    private Integer maCtDonHang;
    
    @Column(name = "ma_don_hang", nullable = false)
    private Integer maDonHang;
    
    @Column(name = "ma_sp", nullable = true)
    private Integer maSp;
    
    @Column(name = "ma_bien_the", nullable = false)
    private Integer maBienThe;
    
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;
    
    @Column(name = "gia", nullable = false)
    private BigDecimal gia;
    
    // Quan hệ với DonHang
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_don_hang", insertable = false, updatable = false)
    private DonHang donHang;
    
    // Quan hệ với SanPham (nếu cần)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sp", insertable = false, updatable = false)
    private SanPham sanPham;
    
    // Quan hệ với BienTheSanPham (nếu cần)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_bien_the", insertable = false, updatable = false)
    private BienTheSanPham bienThe;
}