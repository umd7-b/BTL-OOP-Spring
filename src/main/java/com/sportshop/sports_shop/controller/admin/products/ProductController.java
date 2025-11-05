package com.sportshop.sports_shop.controller.admin.products;

import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.model.ThuongHieu;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportshop.sports_shop.model.AnhSanPham;
import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.DanhMuc;
import com.sportshop.sports_shop.model.MonTheThao;

import com.sportshop.sports_shop.service.SanPhamService;
import com.sportshop.sports_shop.service.ThuongHieuService;
import com.sportshop.sports_shop.service.AnhSanPhamService;
import com.sportshop.sports_shop.service.BienTheSanPhamService;
import com.sportshop.sports_shop.service.DanhMucService;
import com.sportshop.sports_shop.service.MonTheThaoService;
import com.sportshop.sports_shop.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired 
private FileStorageService fileStorageService;
    @Autowired 
    private SanPhamService sanPhamService;
    @Autowired 
    private ThuongHieuService thuongHieuService;
    @Autowired 
    private DanhMucService danhMucService;
    @Autowired 
    private MonTheThaoService monTheThaoService;
    @Autowired private AnhSanPhamService anhSanPhamService;
@Autowired private BienTheSanPhamService bienTheSanPhamService;



    // ✅ Trang danh sách sản phẩm
    @GetMapping("/list")
    public String productsPage(Model model) {
        model.addAttribute("products", sanPhamService.getAll());
        model.addAttribute("brands", thuongHieuService.getAll());
        model.addAttribute("categories", danhMucService.getAll());
        model.addAttribute("sports", monTheThaoService.getAll());

        model.addAttribute("totalProducts", sanPhamService.getAll().size());
        model.addAttribute("totalBrands", thuongHieuService.getAll().size());
        model.addAttribute("totalCategories", danhMucService.getAll().size());
        model.addAttribute("totalSports", monTheThaoService.getAll().size());

        return "admin/products/index";
    }

    // ✅ Add
@PostMapping("/add")
@ResponseBody
public ResponseEntity<?> addProductComplex(
    @ModelAttribute SanPham sanPham, 
    
    // Tên "anhSanPham" này khớp với <input> HTML, không còn xung đột
    @RequestParam(value = "anhSanPham", required = false) List<MultipartFile> files, 
    @RequestParam(value = "maThuongHieu", required = false) Long maThuongHieu,
    @RequestParam(value = "maDanhMuc", required = false) Long maDanhMuc,
    @RequestParam(value = "maMonTheThao", required = false) Long maMonTheThao,
    @RequestParam(value = "variants", required = false) String variantsJson 
) {
    try {
        if (maThuongHieu != null) {
            thuongHieuService.getById(maThuongHieu.intValue()).ifPresent(sanPham::setThuongHieu);
        }
        if (maDanhMuc != null) {
            danhMucService.getById(maDanhMuc.intValue()).ifPresent(sanPham::setDanhMuc);
        }
        // VÀ ĐẢM BẢO BẠN CÓ DÒNG NÀY
        if (maMonTheThao != null) {
            monTheThaoService.getById(maMonTheThao.intValue()).ifPresent(sanPham::setMonTheThao);
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("message", "ID Thương hiệu, Danh mục, hoặc Môn thể thao không hợp lệ."));
    }
    
    // 1. Xử lý Biến thể (Không đổi)
    List<BienTheSanPham> listBienThe = new ArrayList<>();
    if (variantsJson != null && !variantsJson.isEmpty()) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<BienTheSanPham> variants = objectMapper.readValue(variantsJson, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, BienTheSanPham.class));
            
            for (BienTheSanPham bienThe : variants) {
                bienThe.setSanPham(sanPham); 
                listBienThe.add(bienThe);
            }
            sanPham.setBienThe(listBienThe); // Dùng tên biến của bạn
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi xử lý dữ liệu biến thể: " + e.getMessage()));
        }
    }

    // 2. Xử lý lưu ảnh (Đã xóa setAnhDaiDien)
    List<AnhSanPham> listAnh = new ArrayList<>();
    if (files != null && !files.isEmpty()) {
        
        // Vòng lặp này sẽ lưu TẤT CẢ các ảnh
        for (MultipartFile file : files) {
            try {
                String imageName = fileStorageService.save(file);
                AnhSanPham newImage = new AnhSanPham();
                newImage.setLinkAnh(imageName); 
                newImage.setSanPham(sanPham); 
                listAnh.add(newImage);
            } catch (Exception e) { /* Bỏ qua file lỗi */ }
        }
        
        // Gán vào biến đã đổi tên
        sanPham.setDanhSachAnh(listAnh); // <-- Dùng setter mới
    }

    // 3. Lưu sản phẩm
    try {
        sanPhamService.save(sanPham);
        return ResponseEntity.ok().body(Map.of("message", "Thêm sản phẩm thành công"));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("message", "Lỗi khi lưu sản phẩm: " + e.getMessage()));
    }
}


    // ✅ Edit page
    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable Integer id, Model model) {
        SanPham sp = sanPhamService.getById(id.longValue()).orElse(null);
        if (sp == null) return "redirect:/admin/products/list?error=notfound";

        model.addAttribute("product", sp);
        model.addAttribute("brands", thuongHieuService.getAll());
        model.addAttribute("categories", danhMucService.getAll());
        model.addAttribute("sports", monTheThaoService.getAll());

        return "admin/products/edit-product";
    }

    // ✅ Update
    @PostMapping("/update/{id}")
public String updateProduct(
    @PathVariable Integer id,
    @ModelAttribute SanPham sanPham,
    @RequestParam(value = "anhSanPham", required = false) MultipartFile[] files
) throws IOException {
    sanPham.setMaSp(id);
    SanPham updated = sanPhamService.save(sanPham);

    if (files != null && files.length > 0) {
        anhSanPhamService.saveImages(updated, files);
    }

    return "redirect:/admin/products/list?success=update";
}

    // ✅ Delete (AJAX API)
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        sanPhamService.delete(id.longValue());
        Map<String,String> res = new HashMap<>();
        res.put("status","success");
        res.put("message","Xóa sản phẩm thành công");
        return ResponseEntity.ok(res);
    }
    
}
