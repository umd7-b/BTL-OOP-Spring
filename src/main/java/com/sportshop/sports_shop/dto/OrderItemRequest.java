package com.sportshop.sports_shop.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Integer maBienThe;
    private Integer maSp;
    private Integer soLuong;
    private Double donGia;
}
