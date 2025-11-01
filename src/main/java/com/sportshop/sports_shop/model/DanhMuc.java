package com.sportshop.sports_shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DANH_MUC")
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_danh_muc")
    private Integer maDanhMuc;

    @Column(name = "ten_danh_muc", unique = true, nullable = false, length = 100)
    private String tenDanhMuc;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    // TODO: Thêm Constructors, Getters và Setters
}