package com.sportshop.sports_shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data
public class OrderDetailDto {
    private Integer maDonHang;
    private Long maKh;
    private LocalDateTime ngayDat;
    private BigDecimal tongTien;
    private String trangThai;

    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private String diaChiNguoiNhan;

    private String phuongThucThanhToan;

    private List<OrderItemDetailDto> items;
}
