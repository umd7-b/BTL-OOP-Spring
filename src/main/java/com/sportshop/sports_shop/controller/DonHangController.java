package com.sportshop.sports_shop.controller;

import com.sportshop.sports_shop.dto.CreateOrderRequest;
import com.sportshop.sports_shop.dto.OrderResponse;
import com.sportshop.sports_shop.model.DonHang;
import com.sportshop.sports_shop.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donhang")
public class DonHangController {
    
    @Autowired
    private DonHangService donHangService;
    
    // Tạo đơn hàng
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            OrderResponse response = donHangService.createOrder(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    // Lấy đơn hàng theo ID
    @GetMapping("/{maDonHang}")
    public ResponseEntity<?> getOrder(@PathVariable Integer maDonHang) {
        try {
            DonHang donHang = donHangService.getOrderById(maDonHang);
            return ResponseEntity.ok(donHang);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    // Lấy danh sách đơn hàng của khách hàng
    @GetMapping("/customer/{maKh}")
    public ResponseEntity<?> getOrdersByCustomer(@PathVariable Long maKh) {
        try {
            List<DonHang> orders = donHangService.getOrdersByCustomer(maKh);
            return ResponseEntity.ok(orders);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}