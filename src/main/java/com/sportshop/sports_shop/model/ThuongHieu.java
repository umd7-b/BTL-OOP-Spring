package com.sportshop.sports_shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "THUONG_HIEU")
public class ThuongHieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thuong_hieu")
    private Integer maThuongHieu;

    @Column(name = "ten_thuong_hieu", unique = true, nullable = false, length = 100)
    private String tenThuongHieu;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "quoc_gia", length = 50)
    private String quocGia;

    // TODO: Thêm Constructors, Getters và Setters
    // (Bạn nên dùng tính năng tự động tạo của VS Code hoặc Lombok)
}