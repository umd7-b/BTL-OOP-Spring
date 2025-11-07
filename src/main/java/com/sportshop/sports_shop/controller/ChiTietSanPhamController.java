package com.sportshop.sports_shop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportshop.sports_shop.model.AnhSanPham;
import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.service.AnhSanPhamService;
import com.sportshop.sports_shop.service.BienTheSanPhamService;
import com.sportshop.sports_shop.service.SanPhamService;

@Controller
@RequestMapping("/sanpham")
public class ChiTietSanPhamController {

    @Autowired
    private SanPhamService sanPhamService;
    
    @Autowired
    private AnhSanPhamService anhSanPhamService;
    
    @Autowired
    private BienTheSanPhamService bienTheSanPhamService;

    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        System.out.println("========================================");
        System.out.println("🔍 Request nhận được - ID: " + id);
        
        // Lấy thông tin sản phẩm
        Optional<SanPham> sanPhamOpt = sanPhamService.getById(id);
        if (sanPhamOpt.isEmpty()) {
            System.out.println("❌ Không tìm thấy sản phẩm ID: " + id);
            model.addAttribute("error", "Không tìm thấy sản phẩm!");
            return "error";
        }

        SanPham sanPham = sanPhamOpt.get();
        System.out.println("✅ Tìm thấy sản phẩm: " + sanPham.getTenSp());
        
        // Lấy danh sách ảnh sản phẩm
        List<AnhSanPham> danhSachAnh = anhSanPhamService.getByMaSp(id);
        System.out.println("📷 Số lượng ảnh: " + danhSachAnh.size());
        
        // Lấy danh sách biến thể (màu sắc, kích thước)
        List<BienTheSanPham> danhSachBienThe = bienTheSanPhamService.getByMaSp(id.intValue());
        System.out.println("🎨 Số lượng biến thể: " + danhSachBienThe.size());
        
        // Thêm vào model
        model.addAttribute("sanPham", sanPham);
        model.addAttribute("danhSachAnh", danhSachAnh);
        model.addAttribute("danhSachBienThe", danhSachBienThe);
        
        System.out.println("✅ Trả về view: client/home/productdetail");
        System.out.println("========================================");
        
        return "client/home/productdetail";
    }
}