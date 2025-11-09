package com.sportshop.sports_shop.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long maKhachHang;

    private String phuongThucThanhToan; // "COD" hoặc "ONLINE"
    private String tenNguoiNhan;
     private String sdtNguoiNhan;
    private String diaChiNguoiNhan;
    private List<OrderItemRequest> items;
}
