package com.sportshop.sports_shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_don_hang") // Map với bảng 'chi_tiet_don_hang'
@Getter
@Setter
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet")
    private Long maChiTiet;

    // --- Mối quan hệ Nhiều-Một với DonHang (Bên "Nhiều") ---
    // Nhiều chi tiết thuộc về MỘT đơn hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_don_hang", nullable = false) // Tên cột khóa ngoại
    private DonHang donHang;

    // --- Mối quan hệ Nhiều-Một với SanPham ---
    // Nhiều chi tiết có thể trỏ đến MỘT sản phẩm
    // (Giả sử bạn có Entity SanPham.java)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_san_pham", nullable = false)
    private SanPham sanPham;

    // --- Các trường thông tin của chi tiết ---
    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "don_gia", nullable = false, precision = 10, scale = 2)
    private BigDecimal donGia; // Giá của sản phẩm TẠI THỜI ĐIỂM MUA HÀNG
}