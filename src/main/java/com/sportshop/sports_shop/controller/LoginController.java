package com.sportshop.sports_shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sportshop.sports_shop.model.KhachHang;
import com.sportshop.sports_shop.repository.KhachHangRepository;

@Controller
public class LoginController {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Login Client
    @GetMapping("/login")
    public String showUserLoginForm() {
        return "client/home/index.html"; 
    }

    @PostMapping("/login")
    public String processUserLogin(@RequestParam String tenDangNhap,
                                   @RequestParam String matKhau,
                                   Model model) {

        KhachHang kh = khachHangRepository.findByTenDangNhap(tenDangNhap);
        if (kh == null) {
            model.addAttribute("error", "Tên đăng nhập không tồn tại!");
            return "login_user";
        }

        if (!passwordEncoder.matches(matKhau, kh.getMatKhau())) {
            model.addAttribute("error", "Mật khẩu không đúng!");
            return "login_user";
        }

        // Login Success
        return "redirect:/shop/home";
    }

    // Login Admin
    @GetMapping("/login/admin")
    public String showAdminLoginForm() {
        return "admin/login.html"; // trang login_admin.html
    }

    @PostMapping("/login/admin")
    public String processAdminLogin(@RequestParam String tenDangNhap,
                                    @RequestParam String matKhau,
                                    Model model) {

        if("admin".equals(tenDangNhap)&&"123456".equals(matKhau)) {
            return "redirect:/admin/dashboard";
        }else{
              model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
                 return "admin/login.html";
        }

        
    }
}
