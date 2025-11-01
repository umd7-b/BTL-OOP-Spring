package com.sportshop.sports_shop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter; 
import lombok.Setter; 
import java.time.LocalDateTime;

@Entity
@Table(name = "SAN_PHAM")
@Getter
@Setter 
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

    @Column(name = "so_luong_ton")
    private Integer soLuongTon;

    // ... (Giữ nguyên các mối quan hệ @ManyToOne) ...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_thuong_hieu", nullable = false)
    private ThuongHieu thuongHieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_danh_muc", nullable = false)
    private DanhMuc danhMuc;
    
    // ... (Giữ nguyên các trường còn lại) ...
    @Column(name = "kich_thuoc", length = 20)
    private String kichThuoc;

    @Column(name = "mau_sac", length = 50)
    private String mauSac;

    @Column(name = "ngay_them")
    private LocalDateTime ngayThem;
    
    @Column(name = "luot_xem")
    private Integer luotXem;

    @Column(name = "diem_tb")
    private Float diemTb;

    @Transient // Báo cho JPA biết đây KHÔNG PHẢI là một cột trong DB
    public BigDecimal getGiaBan() {
        // Nếu giaKm có giá trị (khác null) VÀ lớn hơn 0
        if (this.giaKm != null && this.giaKm.compareTo(BigDecimal.ZERO) > 0) {
            return this.giaKm; // Thì giá bán là giá khuyến mãi
        }
        // Ngược lại, giá bán là giá gốc
        return this.giaGoc;
    }
}