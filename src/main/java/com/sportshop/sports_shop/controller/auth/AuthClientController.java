package com.sportshop.sports_shop.controller.auth;

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
public class AuthClientController {

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
            if (khachHangRepository.findByTenDangNhap(kh.getTenDangNhap()) != null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Tên đăng nhập đã tồn tại!"));
            }
            if (khachHangRepository.findByEmail(kh.getEmail()) != null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email đã được sử dụng!"));
            }

            kh.setNgayDangKy(LocalDateTime.now());
            khachHangRepository.save(kh);

            return ResponseEntity.ok(Map.of("message", "Đăng ký thành công!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    // ======= XỬ LÝ ĐĂNG NHẬP (POST) =======
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData, HttpSession session) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            if (username == null || username.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Vui lòng nhập tên đăng nhập!"));
            }
            if (password == null || password.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Vui lòng nhập mật khẩu!"));
            }

            KhachHang kh = khachHangRepository.findByTenDangNhap(username);
            if (kh == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Tên đăng nhập không tồn tại!"));
            }
            if (!kh.getMatKhau().equals(password)) {
                return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu không chính xác!"));
            }

            session.setAttribute("khachHang", kh);
            session.setAttribute("userId", kh.getMaKhachHang());
            session.setAttribute("userName", kh.getHoTen());

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", kh.getTenDangNhap());
            userInfo.put("ho_ten", kh.getHoTen());
            userInfo.put("email", kh.getEmail());

            return ResponseEntity.ok(Map.of(
                "message", "Đăng nhập thành công!",
                "user", userInfo
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    // ======= LẤY USER HIỆN TẠI =======
    @GetMapping("/api/auth/me")
    @ResponseBody
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("khachHang");
        if (kh != null) return ResponseEntity.ok(kh);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Chưa đăng nhập"));
    }

    // ======= ĐĂNG XUẤT =======
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công!"));
    }
}
