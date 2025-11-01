package com.sportshop.sports_shop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "khach_hang")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_kh")
    private Long maKhachHang;

    @JsonProperty("username")
    @Column(name = "ten_dang_nhap", nullable = false, unique = true)
    private String tenDangNhap;

    @JsonProperty("password")
    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Transient // không lưu vào DB
    @JsonProperty("confirmPassword")
    private String xacNhanMatKhau;

    @JsonProperty("ho_ten")
    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @JsonProperty("email")
    @Column(name = "email", nullable = false)
    private String email;

    @JsonProperty("so_dien_thoai")
    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @JsonProperty("dia_chi")
    @Column(name = "dia_chi")
    private String diaChi;

    @JsonProperty("ngay_sinh")
    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @JsonProperty("gioi_tinh")
    @Column(name = "gioi_tinh")
    private String gioiTinh;

    @Column(name = "ngay_dang_ky", nullable = false)
    private LocalDateTime ngayDangKy;

    // ===== Constructors =====
    public KhachHang() {
        this.ngayDangKy = LocalDateTime.now(); // Tự động gán khi tạo mới
    }

    // ===== Getters và Setters =====
    public Long getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(Long maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getXacNhanMatKhau() {
        return xacNhanMatKhau;
    }

    public void setXacNhanMatKhau(String xacNhanMatKhau) {
        this.xacNhanMatKhau = xacNhanMatKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDateTime getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDateTime ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    // ===== toString() (tùy chọn để debug) =====
    @Override
    public String toString() {
        return "KhachHang{" +
                "maKhachHang=" + maKhachHang +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", email='" + email + '\'' +
                ", ngayDangKy=" + ngayDangKy +
                '}';
    }
}
