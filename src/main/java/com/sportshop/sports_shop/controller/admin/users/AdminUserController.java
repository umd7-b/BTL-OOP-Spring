package com.sportshop.sports_shop.controller.admin.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sportshop.sports_shop.model.KhachHang;
import com.sportshop.sports_shop.service.DonHangService;
import com.sportshop.sports_shop.service.KhachHangService;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private KhachHangService khachHangService;
    
    @Autowired
    private DonHangService donHangService;

    // Hiển thị danh sách người dùng
    @GetMapping("/users")
    public String getUserList(Model model) {
        model.addAttribute("users", khachHangService.getAllKhachHang());
        return "admin/users/user";
    }

    // API lấy chi tiết người dùng và đơn hàng
    @GetMapping("/users/{id}/detail")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserDetail(@PathVariable Long id) {
        try {
            KhachHang khachHang = khachHangService.getKhachHangById(id);
            
            if (khachHang == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("user", khachHang);
            response.put("totalOrders", donHangService.countOrdersByKhachHang(id));
        
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // API xóa người dùng
    @DeleteMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        try {
            KhachHang khachHang = khachHangService.getKhachHangById(id);
            
            if (khachHang == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Không tìm thấy người dùng");
                  return ResponseEntity.status(404).body(error);
            }

            // Kiểm tra xem có đơn hàng đang xử lý không
            long pendingOrders = donHangService.countPendingOrdersByKhachHang(id);
            if (pendingOrders > 0) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Không thể xóa người dùng có đơn hàng đang xử lý");
                return ResponseEntity.badRequest().body(error);
            }

            khachHangService.deleteKhachHang(id);
            
            Map<String, String> success = new HashMap<>();
            success.put("message", "Xóa người dùng thành công");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Có lỗi xảy ra: Khách hàng đang có thông tin đơn hàng, không thể xóa " );
            return ResponseEntity.internalServerError().body(error);
        }
    }
}