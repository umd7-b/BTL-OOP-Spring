package com.sportshop.sports_shop.controller.admin.orders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sportshop.sports_shop.dto.OrderItemDetailDto;
import com.sportshop.sports_shop.dto.OrderResponse;
import com.sportshop.sports_shop.dto.OrderSummaryDto;
import com.sportshop.sports_shop.service.DonHangService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    
    @Autowired
    private DonHangService donHangService;
    

    /**
     * ✅ API: Lấy tất cả đơn hàng (Dùng DTO)
     */
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderSummaryDto> orders = donHangService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Lỗi khi tải danh sách đơn hàng: " + e.getMessage()));
        }
    }

    /**
     * ✅ API: Lấy thống kê đơn hàng
     */
    @GetMapping("/api/statistics")
    @ResponseBody
    public ResponseEntity<?> getOrderStatistics() {
        try {
            Map<String, Object> stats = donHangService.getOrderStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Lỗi khi tải thống kê: " + e.getMessage()));
        }
    }

    /**
     * ✅ API: Lấy chi tiết đơn hàng
     */
    @GetMapping("/api/{maDonHang}")
    @ResponseBody
    public ResponseEntity<?> getOrderDetail(@PathVariable Integer maDonHang) {
        try {
            OrderResponse order = donHangService.getDonHangById(maDonHang);
            List<OrderItemDetailDto> items = donHangService.getChiTietDonHang(maDonHang);
            
            Map<String, Object> response = new HashMap<>();
            response.put("order", order);
            response.put("items", items);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Lỗi khi tải chi tiết đơn hàng: " + e.getMessage()));
        }
    }

    /**
     * ✅ API: Cập nhật trạng thái đơn hàng
     */
    @PutMapping("/api/{maDonHang}/status")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer maDonHang,
            @RequestBody Map<String, String> request) {
        try {
            String newStatus = request.get("status");
            OrderResponse result = donHangService.updateOrderStatus(maDonHang, newStatus);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Lỗi khi cập nhật trạng thái: " + e.getMessage()));
        }
    }

    /**
     * ✅ API: Xóa đơn hàng
     */
    @DeleteMapping("/api/{maDonHang}")
    @ResponseBody
    public ResponseEntity<?> deleteOrder(@PathVariable Integer maDonHang) {
        try {
            donHangService.deleteOrder(maDonHang);
            return ResponseEntity.ok(Map.of("message", "Xóa đơn hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Lỗi khi xóa đơn hàng: " + e.getMessage()));
        }
    }
}
