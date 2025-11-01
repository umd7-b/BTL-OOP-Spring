package com.sportshop.sports_shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal; // Dùng BigDecimal cho tiền tệ để tránh sai số
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "don_hang") // Map với bảng 'don_hang' trong DB
@Getter
@Setter
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don_hang")
    private Long maDonHang;

    // --- Mối quan hệ Nhiều-Một với KhachHang ---
    // Nhiều đơn hàng có thể thuộc về MỘT khách hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_khach_hang", nullable = false) // Tên cột khóa ngoại
    private KhachHang khachHang;

    // --- Các trường thông tin của đơn hàng ---
    @Column(name = "ngay_dat_hang", nullable = false)
    private LocalDateTime ngayDatHang;

    @Column(name = "tong_tien", nullable = false, precision = 10, scale = 2)
    private BigDecimal tongTien; // Dùng BigDecimal cho chính xác

    @Column(name = "trang_thai", length = 50)
    private String trangThai; // Ví dụ: "Chờ xử lý", "Đã giao", "Đã hủy"

    @Column(name = "dia_chi_giao_hang")
    private String diaChiGiaoHang;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    // --- Mối quan hệ Một-Nhiều với ChiTietDonHang ---
    // MỘT đơn hàng có NHIỀU chi tiết đơn hàng
    @OneToMany(
        mappedBy = "donHang", // "donHang" là tên trường ở class ChiTietDonHang
        cascade = CascadeType.ALL, // Lưu/Xóa chi tiết khi lưu/xóa đơn hàng
        orphanRemoval = true // Xóa chi tiết nếu nó bị gỡ khỏi danh sách
    )
    private Set<ChiTietDonHang> chiTietDonHangs = new HashSet<>();

    // --- Các hàm trợ giúp (Helper methods) để đồng bộ hai chiều (Rất quan trọng) ---
    public void addChiTiet(ChiTietDonHang chiTiet) {
        chiTietDonHangs.add(chiTiet);
        chiTiet.setDonHang(this);
    }

    public void removeChiTiet(ChiTietDonHang chiTiet) {
        chiTietDonHangs.remove(chiTiet);
        chiTiet.setDonHang(null);
    }
}