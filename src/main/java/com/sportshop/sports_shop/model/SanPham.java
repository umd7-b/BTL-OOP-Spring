package com.sportshop.sports_shop.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "san_pham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_sp")
    private Integer maSp;


    @Column(name = "ten_sp", length = 100)
    private String tenSp;


    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;


    @Column(name = "gia_goc", precision = 10, scale = 2)
    private BigDecimal giaGoc;


    @Column(name = "gia_km", precision = 10, scale = 2)
    private BigDecimal giaKm;


    @Column(name = "ngay_them")
    private LocalDateTime ngayThem;


    @ManyToOne
    @JoinColumn(name = "ma_thuong_hieu")
    private ThuongHieu thuongHieu;


    @ManyToOne
    @JoinColumn(name = "ma_danh_muc")
    private DanhMuc danhMuc;
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong = 0; // default 0


    public Integer getSoLuong() {
        return soLuong;
    }


    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }


    @ManyToOne
    @JoinColumn(name = "ma_mon_the_thao")
    private MonTheThao monTheThao;


    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BienTheSanPham> bienThe;


    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnhSanPham> danhSachAnh = new ArrayList<>();


    public List<AnhSanPham> getDanhSachAnh() {
        return danhSachAnh;
    }


    public void setDanhSachAnh(List<AnhSanPham> danhSachAnh) {
        this.danhSachAnh = danhSachAnh;
    }


    public String getAnhDaiDien() {
        if (danhSachAnh != null && !danhSachAnh.isEmpty()) {
            return danhSachAnh.get(0).getLinkAnh();
        }
        return "/assets/img/no-image.jpg"; // ảnh default
    }


    public Integer getTongSoLuongTon() {
        // 1. Kiểm tra xem danh sách biến thể có bị null hoặc rỗng không
        // (Hãy đảm bảo tên biến "bienTheSanPham" là chính xác)
        if (this.bienThe == null || this.bienThe.isEmpty()) {
            return 0;
        }


        // 2. Dùng Stream API để tính tổng "soLuongTon"
        return this.bienThe.stream()
                .mapToInt(bienThe
                        -> // Kiểm tra null cho từng "soLuongTon"
                        (bienThe.getSoLuongTon() != null) ? bienThe.getSoLuongTon() : 0
                )
                .sum();
    }
   


}
