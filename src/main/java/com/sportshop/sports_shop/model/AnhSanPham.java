package com.sportshop.sports_shop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"sanPham", "danhSachAnh", "bienThe"}) // tránh lặp vô hạn

@Entity
@Table(name = "anh_san_pham")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AnhSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_anh")
    private Integer maAnh;

    @Column(name = "link_anh", length = 255, nullable = false)
    private String linkAnh;

    @ManyToOne
    @JoinColumn(name = "ma_sp", nullable = false)
    private SanPham sanPham;

}
 
    

