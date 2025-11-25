package com.sportshop.sports_shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sportshop.sports_shop.dto.CartItemDTO;
import com.sportshop.sports_shop.model.ChiTietGioHang;
import com.sportshop.sports_shop.service.GioHangService;
import com.sportshop.sports_shop.service.impl.GioHangServiceImpl;

@RestController
@RequestMapping("/api/giohang")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    // ✅ 1) Thêm vào giỏ hàng - TRẢ VỀ MAP ĐƠN GIẢN
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long maKhachHang,
            @RequestParam Integer maBienThe,
            @RequestParam Integer soLuong,
            @RequestParam Double gia
    ) {
        try {
            ChiTietGioHang result = gioHangService.addToCart(maKhachHang, maBienThe, soLuong, gia, true);
            
            // Tạo response đơn giản (không có nested objects để tránh lỗi JSON)
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thêm vào giỏ hàng thành công");
            response.put("maCtGioHang", result.getMaCtGioHang());
            response.put("soLuong", result.getSoLuong());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ✅ 2) Lấy giỏ hàng của 1 khách - TRẢ VỀ DTO
    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam Long maKhachHang) {
        try {
            // Cast sang implementation để gọi method getCartDTO
            GioHangServiceImpl serviceImpl = (GioHangServiceImpl) gioHangService;
            List<CartItemDTO> cart = serviceImpl.getCartDTO(maKhachHang);
            return ResponseEntity.ok(cart);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi khi lấy giỏ hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ✅ 3) Cập nhật số lượng
    @PutMapping("/capnhatsl")
    public ResponseEntity<?> updateQuantity(
            @RequestParam Integer maCtGioHang,
            @RequestParam Integer soLuong
    ) {
        try {
            ChiTietGioHang result = gioHangService.updateQuantity(maCtGioHang, soLuong);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật số lượng thành công");
            response.put("soLuong", result.getSoLuong());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ✅ 4) Xóa 1 sản phẩm khỏi giỏ
    @DeleteMapping("/xoa")
    public ResponseEntity<?> deleteItem(
            @RequestParam Long maKhachHang,
            @RequestParam Integer maBienThe
    ) {
        try {
            gioHangService.deleteItem(maKhachHang, maBienThe);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã xóa sản phẩm khỏi giỏ hàng");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
             System.err.println("=== ERROR DELETE ===");
            
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

  
    
   
}