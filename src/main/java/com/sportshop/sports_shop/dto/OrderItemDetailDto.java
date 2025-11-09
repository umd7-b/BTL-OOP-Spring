package com.sportshop.sports_shop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetailDto {
    
    private Integer maCtDonHang;
    private Integer maSp;
    private String tenSanPham;
    private String hinhAnh;
    private Integer maBienThe;
    private String tenBienThe;  // Ví dụ: "Màu Đỏ - Size M"
    private Integer soLuong;
    private BigDecimal gia;
    private BigDecimal thanhTien;  // soLuong * gia
    
    // Constructor tiện dụng
    public OrderItemDetailDto(Integer maCtDonHang, Integer maSp, String tenSanPham, 
                             String hinhAnh, Integer maBienThe, String tenBienThe,
                             Integer soLuong, BigDecimal gia) {
        this.maCtDonHang = maCtDonHang;
        this.maSp = maSp;
        this.tenSanPham = tenSanPham;
        this.hinhAnh = hinhAnh;
        this.maBienThe = maBienThe;
        this.tenBienThe = tenBienThe;
        this.soLuong = soLuong;
        this.gia = gia;
        this.thanhTien = gia.multiply(BigDecimal.valueOf(soLuong));
    }
}