package com.sportshop.sports_shop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"sanPham", "danhSachAnh", "bienThe"}) // tránh lặp vô hạn


@Entity
@Table(name = "bien_the_san_pham",
       uniqueConstraints = @UniqueConstraint(columnNames = {"ma_sp", "mau_sac", "kich_thuoc"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BienTheSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maBienThe;

    @ManyToOne
    @JoinColumn(name = "ma_sp")
    private SanPham sanPham;

    private String mauSac;

    private String kichThuoc;

    private Integer soLuongTon;
}
