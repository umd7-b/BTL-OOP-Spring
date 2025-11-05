package com.sportshop.sports_shop.controller.admin.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminMainController {

    private boolean isAuthenticated(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        return isAdmin != null && isAdmin;
    }



    @GetMapping("/admin/users")
    public String users(HttpSession session, Model model) {
        if (!isAuthenticated(session)) return "redirect:/admin/login";
        model.addAttribute("pageTitle", "Quản lý người dùng");
        return "admin/users/index";
    }

    
    @GetMapping("/admin/products")
public String products(HttpSession session) {
    if (!isAuthenticated(session)) return "redirect:/admin/login";
    return "redirect:/admin/products/list";
}

    // Orders management
    @GetMapping("/admin/orders")
    public String orders(HttpSession session, Model model) {
        if (!isAuthenticated(session)) return "redirect:/admin/login";
        model.addAttribute("pageTitle", "Quản lý đơn hàng");
        return "admin/orders/index";
    }

    // Statistics/Analytics
    @GetMapping("/admin/analytics")
    public String analytics(HttpSession session, Model model) {
        if (!isAuthenticated(session)) return "redirect:/admin/login";
        model.addAttribute("pageTitle", "Thống kê doanh thu");
        return "admin/analytics/index";
    }

    // Settings
    @GetMapping("/admin/settings")
    public String settings(HttpSession session, Model model) {
        if (!isAuthenticated(session)) return "redirect:/admin/login";
        model.addAttribute("pageTitle", "Cài đặt hệ thống");
        return "admin/settings/index";
    }
}
