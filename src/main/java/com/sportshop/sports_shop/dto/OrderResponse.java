package com.sportshop.sports_shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderResponse {

    private Integer maDonHang;
    private Long maKh;
    private LocalDateTime ngayDat;
    private BigDecimal tongTien;
    private String trangThai;

    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String diaChiNguoiNhan;

    private String phuongThucThanhToan;

    private String message;
}
