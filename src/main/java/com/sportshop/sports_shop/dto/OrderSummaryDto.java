package com.sportshop.sports_shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderSummaryDto {
    private Integer maDonHang;
    private Long maKh;
    private LocalDateTime ngayDat;
    private BigDecimal tongTien;
    private String trangThai;
    private String tenNguoiNhan;
}
