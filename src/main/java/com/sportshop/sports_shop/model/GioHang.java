package com.sportshop.sports_shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gio_hang") // Map với bảng 'gio_hang'
@Getter
@Setter
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_gio_hang")
    private Long maGioHang;

    // --- Mối quan hệ Một-Một với KhachHang ---
    // MỘT giỏ hàng chỉ thuộc về MỘT khách hàng
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_khach_hang", nullable = false, unique = true) // Khóa ngoại, phải là duy nhất (unique)
    private KhachHang khachHang;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    // --- Mối quan hệ Một-Nhiều với ChiTietGioHang ---
    // MỘT giỏ hàng có NHIỀU chi tiết (mặt hàng)
    @OneToMany(
        mappedBy = "gioHang", // "gioHang" là tên trường ở class ChiTietGioHang
        cascade = CascadeType.ALL, // Lưu/Xóa chi tiết khi lưu/xóa giỏ hàng
        orphanRemoval = true // Xóa chi tiết nếu nó bị gỡ khỏi danh sách
    )
    private Set<ChiTietGioHang> chiTietGioHangs = new HashSet<>();

    // --- (Tùy chọn) Hàm khởi tạo ---
    public GioHang() {
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // --- Các hàm trợ giúp (Helper methods) để đồng bộ ---
    public void addChiTiet(ChiTietGioHang chiTiet) {
        chiTietGioHangs.add(chiTiet);
        chiTiet.setGioHang(this);
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void removeChiTiet(ChiTietGioHang chiTiet) {
        chiTietGioHangs.remove(chiTiet);
        chiTiet.setGioHang(null);
        this.ngayCapNhat = LocalDateTime.now();
    }
}