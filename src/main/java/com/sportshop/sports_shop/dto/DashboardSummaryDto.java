package com.sportshop.sports_shop.dto;

import java.math.BigDecimal;

public class DashboardSummaryDto {

    private long totalUsers;      // tổng khách hàng
    private long totalOrders;     // tổng đơn hàng
    private long totalProducts;   // tổng sản phẩm
    private BigDecimal totalRevenue; // tổng doanh thu (đơn đã hoàn thành)

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
