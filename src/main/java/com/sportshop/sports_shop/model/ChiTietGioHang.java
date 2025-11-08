package com.sportshop.sports_shop.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chi_tiet_gio_hang")
@Data
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChiTietGioHang {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ct_gio_hang")
    private Integer maCtGioHang;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_gio_hang", nullable = false)
    @JsonIgnoreProperties({"chiTietGioHang"}) // Tránh vòng lặp
    private GioHang gioHang;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_bien_the", nullable = false)
    @JsonIgnoreProperties({"chiTietGioHang", "sanPham"}) // Tránh vòng lặp
    private BienTheSanPham bienThe;
    
    @Column(name = "ma_sp", nullable = false)
    private Integer maSp;
    
    @Column(name = "so_luong")
    private Integer soLuong;
    
    @Column(name = "gia")
    private BigDecimal gia;
    
    @Column(name = "da_chon")
    private Boolean daChon = true;
}