
package com.sportshop.sports_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Integer maCtGioHang;
    private Integer maBienThe;
    private Integer maSp;
    private String tenSp;
    private String hinhAnh;
    private String phanLoaiText; // "Đỏ - XL"
    private Integer soLuong;
    private Double gia;
    private Boolean daChon;
}