package com.sportshop.sports_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // <-- Thêm import này

@Controller
public class RenderTrangDonHang {

    // (1) Controller cho trang DANH SÁCH (cái này bạn đã có)
    @GetMapping("/order")
    public String orderPage() {
        // Trả về file: client/home/order.html
        return "client/home/order"; 
    }

    // (2) BỔ SUNG: Controller cho trang CHI TIẾT
    @GetMapping("/order/detail/{maDonHang}")
    public String orderDetailPage(@PathVariable Integer maDonHang) {
        // Bạn sẽ cần tạo một file HTML mới tên là order-detail.html
        // để hiển thị chi tiết đơn hàng này
        return "client/home/order-detail"; 
    }
}