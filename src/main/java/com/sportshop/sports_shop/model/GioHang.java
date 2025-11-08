    package com.sportshop.sports_shop.model;

    import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Data;
import lombok.NoArgsConstructor;
    

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Entity
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Table(name = "gio_hang")
    public class GioHang {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ma_gio_hang")
        private Integer maGioHang;
        
        @Column(name = "ma_kh") // ✅ Phải khớp với tên cột trong DB
        private Long maKh; // hoặc maKhachHang
        
        @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL)
        private List<ChiTietGioHang> chiTietList;
        
    // Getters & Setters
}