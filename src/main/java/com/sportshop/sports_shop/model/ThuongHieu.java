package com.sportshop.sports_shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "thuong_hieu")
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

    // ---------------- Constructors ----------------

    public ThuongHieu() {}

    public ThuongHieu(Integer maThuongHieu, String tenThuongHieu, String moTa, String logoUrl, String quocGia) {
        this.maThuongHieu = maThuongHieu;
        this.tenThuongHieu = tenThuongHieu;
        this.moTa = moTa;
        this.logoUrl = logoUrl;
        this.quocGia = quocGia;
    }

    // ---------------- Getters & Setters ----------------

    public Integer getMaThuongHieu() {
        return maThuongHieu;
    }

    public void setMaThuongHieu(Integer maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getQuocGia() {
        return quocGia;
    }

    public void setQuocGia(String quocGia) {
        this.quocGia = quocGia;
    }
}
