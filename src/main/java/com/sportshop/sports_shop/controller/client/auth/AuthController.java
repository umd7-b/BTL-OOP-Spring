package com.sportshop.sports_shop.controller.client.auth;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sportshop.sports_shop.model.KhachHang;
import com.sportshop.sports_shop.repository.KhachHangRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private KhachHangRepository khachHangRepository;

    // ======= TRANG LOGIN (HTML VIEW) =======
    @GetMapping("/login")
    public String loginPage() {
        return "client/auth/login";
    }

    // ======= TRANG REGISTER (HTML VIEW) =======
    @GetMapping("/register")
    public String registerPage() {
        return "client/auth/register";
    }

    // ======= XỬ LÝ ĐĂNG KÝ (POST) =======
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody KhachHang kh) {
        try {
            // Kiểm tra trùng tên đăng nhập
            if (khachHangRepository.findByTenDangNhap(kh.getTenDangNhap()) != null) {
                return ResponseEntity.badRequest().body("{\"message\":\"Tên đăng nhập đã tồn tại!\"}");
            }

            // Kiểm tra trùng email
            if (khachHangRepository.findByEmail(kh.getEmail()) != null) {
                return ResponseEntity.badRequest().body("{\"message\":\"Email đã được sử dụng!\"}");
            }

            kh.setNgayDangKy(LocalDateTime.now());
            khachHangRepository.save(kh);

            return ResponseEntity.ok("{\"message\":\"Đăng ký thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"message\":\"Lỗi server: " + e.getMessage() + "\"}");
        }
    }

    // ======= XỬ LÝ ĐĂNG NHẬP (POST) =======
      @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData, 
                                      HttpSession session) {
        System.out.println("=== Nhận request login ===");
        System.out.println("Data: " + loginData);
        
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

            if (username == null || username.trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Vui lòng nhập tên đăng nhập!");
                return ResponseEntity.badRequest().body(response);
            }

            if (password == null || password.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Vui lòng nhập mật khẩu!");
                return ResponseEntity.badRequest().body(response);
            }

            KhachHang khachHang = khachHangRepository.findByTenDangNhap(username);
            System.out.println("Tìm khách hàng: " + (khachHang != null ? "Có" : "Không"));

            if (khachHang == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Tên đăng nhập không tồn tại!");
                return ResponseEntity.badRequest().body(response);
            }

            if (!khachHang.getMatKhau().equals(password)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Mật khẩu không chính xác!");
                return ResponseEntity.badRequest().body(response);
            }

            // Lưu session
            session.setAttribute("khachHang", khachHang);
            session.setAttribute("userId", khachHang.getMaKhachHang());
            session.setAttribute("userName", khachHang.getHoTen());

            System.out.println("=== Đăng nhập thành công ===");

            // Trả về response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng nhập thành công!");
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", khachHang.getTenDangNhap());
            userInfo.put("ho_ten", khachHang.getHoTen());
            userInfo.put("email", khachHang.getEmail());
            userInfo.put("maKhachHang", khachHang.getMaKhachHang());
            
            response.put("user", userInfo);

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("=== LỖI: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi server: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @GetMapping("/api/auth/me")
        @ResponseBody
        public ResponseEntity<?> getCurrentUser(HttpSession session) {
            KhachHang kh = (KhachHang) session.getAttribute("khachHang");
            if (kh != null) {
                return ResponseEntity.ok(kh); // Trả về thông tin user nếu còn
            }
            // Trả về lỗi 401 nếu không có ai trong session
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

    // ======= XỬ LÝ ĐĂNG XUẤT (POST) =======
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logoutUser(HttpSession session) {
        try {
            session.invalidate();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Đăng xuất thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi server: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}