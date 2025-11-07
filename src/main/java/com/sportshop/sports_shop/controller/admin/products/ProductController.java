package com.sportshop.sports_shop.controller.admin.products;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportshop.sports_shop.model.AnhSanPham;
import com.sportshop.sports_shop.model.BienTheSanPham;
import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.service.AnhSanPhamService;
import com.sportshop.sports_shop.service.BienTheSanPhamService;
import com.sportshop.sports_shop.service.DanhMucService;
import com.sportshop.sports_shop.service.FileStorageService;
import com.sportshop.sports_shop.service.MonTheThaoService;
import com.sportshop.sports_shop.service.SanPhamService;
import com.sportshop.sports_shop.service.ThuongHieuService;


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
   
   
    SanPham sp = sanPhamService.getByIdFull(id.longValue()).orElse(null); // <-- DÒNG MỚI


    if (sp == null) return "redirect:/admin/products/list?error=notfound";


    model.addAttribute("product", sp);
    model.addAttribute("brands", thuongHieuService.getAll());
    model.addAttribute("categories", danhMucService.getAll());
    model.addAttribute("sports", monTheThaoService.getAll());


    return "admin/products/edit-product";
}


 @PostMapping("/update/{id}")
 @Transactional
public String updateProduct(
        @PathVariable Integer id,
        @ModelAttribute SanPham sanPhamFromForm, // Đối tượng từ form
        @RequestParam(value = "anhSanPham", required = false) MultipartFile[] files,
        RedirectAttributes redirectAttributes
) throws IOException {


    // 1. Lấy sản phẩm hiện có từ DB (dùng getByIdFull để có cả ảnh + biến thể)
    SanPham existingProduct = sanPhamService.getByIdFull(id.longValue()).orElse(null);
    if (existingProduct == null) {
        redirectAttributes.addFlashAttribute("error", "Lỗi: Không tìm thấy sản phẩm.");
        return "redirect:/admin/products/list";
    }


    // 2. Cập nhật thông tin cơ bản từ form vào sản phẩm vừa lấy từ DB
    existingProduct.setTenSp(sanPhamFromForm.getTenSp());
    existingProduct.setGiaGoc(sanPhamFromForm.getGiaGoc());
    existingProduct.setGiaKm(sanPhamFromForm.getGiaKm());
    existingProduct.setThuongHieu(sanPhamFromForm.getThuongHieu());
    existingProduct.setDanhMuc(sanPhamFromForm.getDanhMuc());
    existingProduct.setMonTheThao(sanPhamFromForm.getMonTheThao());
    // (Thêm các trường khác nếu có, ví dụ: moTa)


   
    // --- BẮT ĐẦU PHẦN LƯU BIẾN THỂ ---


    // 3. Xóa tất cả các biến thể cũ khỏi sản phẩm
    // (JPA sẽ tự động xóa trong DB nhờ orphanRemoval=true)
    existingProduct.getBienThe().clear();


    // 4. Thêm các biến thể mới từ form
    if (sanPhamFromForm.getBienThe() != null) {
        for (BienTheSanPham variantFromForm : sanPhamFromForm.getBienThe()) {
           
            // QUAN TRỌNG: Gán sản phẩm cha (existingProduct) cho biến thể con
            variantFromForm.setSanPham(existingProduct);
           
            // Thêm biến thể mới vào danh sách
            existingProduct.getBienThe().add(variantFromForm);
        }
    }
    // --- KẾT THÚC PHẦN LƯU BIẾN THỂ ---




    // 5. Lưu sản phẩm (CHỈ MỘT LẦN)
    // CascadeType.ALL sẽ tự động lưu các thay đổi của biến thể
    SanPham updatedProduct = sanPhamService.save(existingProduct);




    // 6. Xử lý upload ảnh
    boolean hasRealFile = false;
    if (files != null) {
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                hasRealFile = true;
                break;
            }
        }
    }


    if (hasRealFile) {
        // Tùy chọn: Bạn có thể muốn xóa ảnh cũ trước khi thêm ảnh mới
        // anhSanPhamService.deleteImages(updatedProduct);
       
        anhSanPhamService.saveImages(updatedProduct, files);
    }


    // 7. Thông báo thành công
    redirectAttributes.addFlashAttribute("success", "✅ Cập nhật sản phẩm thành công!");


    return "redirect:/admin/products/edit/" + id;
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



