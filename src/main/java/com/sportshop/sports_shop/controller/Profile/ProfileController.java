package com.sportshop.sports_shop.controller.Profile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sportshop.sports_shop.model.KhachHang;
import com.sportshop.sports_shop.repository.KhachHangRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private KhachHangRepository khachHangRepository;

    // ======= TRANG THÔNG TIN CÁ NHÂN =======
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");
        
        if (khachHang == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", khachHang);
        return "client/profile/index";
    }

    // ======= API LẤY THÔNG TIN USER =======
    @GetMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<?> getProfile(HttpSession session) {
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");
        
        if (khachHang == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Chưa đăng nhập!");
            return ResponseEntity.status(401).body(response);
        }
        
        // Lấy thông tin mới nhất từ database
        KhachHang user = khachHangRepository.findById(khachHang.getMaKhachHang()).orElse(null);
        
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Không tìm thấy thông tin người dùng!");
            return ResponseEntity.status(404).body(response);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("maKhachHang", user.getMaKhachHang());
        userInfo.put("username", user.getTenDangNhap());
        userInfo.put("hoTen", user.getHoTen());
        userInfo.put("email", user.getEmail());
        userInfo.put("soDienThoai", user.getSoDienThoai());
        userInfo.put("diaChi", user.getDiaChi());
        userInfo.put("gioiTinh", user.getGioiTinh());
        userInfo.put("ngayDangKy", user.getNgayDangKy());
        
        response.put("user", userInfo);
        
        return ResponseEntity.ok(response);
    }

    // ======= API CẬP NHẬT THÔNG TIN =======
    @PostMapping("/api/profile/update")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updateData, 
                                          HttpSession session) {
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");
        
        if (khachHang == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Chưa đăng nhập!");
            return ResponseEntity.status(401).body(response);
        }
        
        try {
            KhachHang user = khachHangRepository.findById(khachHang.getMaKhachHang()).orElse(null);
            
            if (user == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Không tìm thấy thông tin người dùng!");
                return ResponseEntity.status(404).body(response);
            }
            
            // Cập nhật thông tin
            if (updateData.get("hoTen") != null) {
                user.setHoTen(updateData.get("hoTen"));
            }
            if (updateData.get("email") != null) {
                // Kiểm tra email trùng
                KhachHang existingEmail = khachHangRepository.findByEmail(updateData.get("email"));
                if (existingEmail != null && !existingEmail.getMaKhachHang().equals(user.getMaKhachHang())) {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Email đã được sử dụng!");
                    return ResponseEntity.badRequest().body(response);
                }
                user.setEmail(updateData.get("email"));
            }
            if (updateData.get("soDienThoai") != null) {
                user.setSoDienThoai(updateData.get("soDienThoai"));
            }
            if (updateData.get("diaChi") != null) {
                user.setDiaChi(updateData.get("diaChi"));
            }
            if (updateData.get("gioiTinh") != null) {
                user.setGioiTinh(updateData.get("gioiTinh"));
            }
            
            khachHangRepository.save(user);
            
            // Cập nhật lại session
            session.setAttribute("khachHang", user);
            session.setAttribute("userName", user.getHoTen());
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cập nhật thông tin thành công!");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi server: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ======= API ĐỔI MẬT KHẨU =======
    @PostMapping("/api/profile/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData, 
                                           HttpSession session) {
        KhachHang khachHang = (KhachHang) session.getAttribute("khachHang");
        
        if (khachHang == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Chưa đăng nhập!");
            return ResponseEntity.status(401).body(response);
        }
        
        try {
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (oldPassword == null || newPassword == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Vui lòng nhập đầy đủ thông tin!");
                return ResponseEntity.badRequest().body(response);
            }
            
            KhachHang user = khachHangRepository.findById(khachHang.getMaKhachHang()).orElse(null);
            
            if (user == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Không tìm thấy thông tin người dùng!");
                return ResponseEntity.status(404).body(response);
            }
            
            // Kiểm tra mật khẩu cũ
            if (!user.getMatKhau().equals(oldPassword)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Mật khẩu cũ không chính xác!");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Cập nhật mật khẩu mới
            user.setMatKhau(newPassword);
            khachHangRepository.save(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Đổi mật khẩu thành công!");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi server: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}