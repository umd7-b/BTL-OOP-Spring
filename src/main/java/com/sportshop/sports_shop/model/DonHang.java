package com.sportshop.sports_shop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "don_hang")
@Data
public class DonHang {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don_hang")
    private Integer maDonHang;
    
    @Column(name = "ma_kh", nullable = false)
    private Long maKh;
    
    @Column(name = "ngay_dat")
    private LocalDateTime ngayDat;
    
    @Column(name = "tong_tien", nullable = false)
    private BigDecimal tongTien;
    
    @Column(name = "trang_thai", length = 50)
    private String trangThai = "CHO_XAC_NHAN";
    
    @Column(name = "dia_chi_giao")
    private String diaChiGiao;
    
    @Column(name = "phuong_thuc_thanh_toan", length = 50)
    private String phuongThucThanhToan;
    
    // Quan hệ với KhachHang (nếu cần)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_kh", insertable = false, updatable = false)
    private KhachHang khachHang;
    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "sdt_nguoi_nhan")
    private String sdtNguoiNhan;

    @Column(name = "dia_chi_nguoi_nhan")
    private String diaChiNguoiNhan;

    // Quan hệ với ChiTietDonHang
    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("donHang") 
    private List<ChiTietDonHang> chiTietDonHangs;
    
    @PrePersist
    public void prePersist() {
        if (ngayDat == null) {
            ngayDat = LocalDateTime.now();
        }
    }
}