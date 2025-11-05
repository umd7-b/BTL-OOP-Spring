package com.sportshop.sports_shop.controller.admin.products;

import com.sportshop.sports_shop.model.ThuongHieu;
import com.sportshop.sports_shop.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/brands")
public class BrandController {

    @Autowired
    private ThuongHieuService thuongHieuService;

    /**
     * Lấy danh sách thương hiệu
     */
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<?> getAllBrands() {
        return ResponseEntity.ok(thuongHieuService.getAll());
    }

    /**
     * Thêm thương hiệu
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addBrand(@ModelAttribute ThuongHieu thuongHieu) {
        Map<String, String> res = new HashMap<>();

        if (thuongHieuService.existsByName(thuongHieu.getTenThuongHieu())) {
            res.put("status", "error");
            res.put("message", "Thương hiệu đã tồn tại");
            return ResponseEntity.badRequest().body(res);
        }

        thuongHieuService.save(thuongHieu);
        res.put("status", "success");
        res.put("message", "Thêm thương hiệu thành công");

        return ResponseEntity.ok(res);
    }

    /**
     * Cập nhật thương hiệu
     */
    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateBrand(
            @PathVariable Integer id,
            @ModelAttribute ThuongHieu thuongHieu
    ) {
        Map<String, String> res = new HashMap<>();

        thuongHieu.setMaThuongHieu(id);
        thuongHieuService.save(thuongHieu);

        res.put("status", "success");
        res.put("message", "Cập nhật thương hiệu thành công");
        return ResponseEntity.ok(res);
    }

    /**
     * Xóa thương hiệu
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id) {
        Map<String, String> res = new HashMap<>();

        thuongHieuService.delete(id);

        res.put("status", "success");
        res.put("message", "Xóa thương hiệu thành công");
        return ResponseEntity.ok(res);
    }
}
