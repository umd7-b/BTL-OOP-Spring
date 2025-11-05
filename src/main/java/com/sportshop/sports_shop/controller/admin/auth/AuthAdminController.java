package com.sportshop.sports_shop.controller.admin.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthAdminController {

    
    @GetMapping("/admin")
    public String adminRoot(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        if (isAdmin == null || !isAdmin) {
            // Nếu CHƯA đăng nhập -> về trang login
            return "redirect:/admin/login";
        } else {
            // Nếu ĐÃ đăng nhập -> vào trang dashboard
            return "redirect:/admin/dashboard";
        }
    }



   
    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/auth/login"; // templates/admin/auth/login.html
    }

    // ======= XỬ LÝ ĐĂNG NHẬP ADMIN =======
    @PostMapping("/admin/login")
    @ResponseBody
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> loginData, HttpSession session) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            if (username == null || username.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Vui lòng nhập tên đăng nhập!"));
            }
            if (password == null || password.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Vui lòng nhập mật khẩu!"));
            }

            // ✅ Kiểm tra tài khoản cố định
            if (!"admin".equals(username) || !"123456".equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Tên đăng nhập hoặc mật khẩu không chính xác!"));
            }

            // ✅ Lưu session admin
            session.setAttribute("isAdmin", true);
            session.setAttribute("adminUsername", username);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng nhập quản trị thành công!");
            response.put("redirect", "/admin/dashboard");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    // ======= DASHBOARD ADMIN =======
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            // ✅ Đã sửa lại đường dẫn redirect cho đúng
            return "redirect:/admin/login";
        }
        return "admin/dashboard/index";
    }

    // ======= ĐĂNG XUẤT ADMIN =======
    @PostMapping("/admin/logout")
    @ResponseBody
    public ResponseEntity<?> logoutAdmin(HttpSession session) {
        session.invalidate();
        
        // Trả về thông báo + đường dẫn để client tự redirect
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng xuất admin thành công!");
        response.put("redirect", "/admin/login");

        return ResponseEntity.ok(response);
    }
}