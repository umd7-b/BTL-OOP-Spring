package com.sportshop.sports_shop.controller.admin.products;

import com.sportshop.sports_shop.model.DanhMuc;
import com.sportshop.sports_shop.model.MonTheThao;
import com.sportshop.sports_shop.service.DanhMucService;
import com.sportshop.sports_shop.service.MonTheThaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CategorySportController {

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    private MonTheThaoService monTheThaoService;

    // ================== CATEGORY API ==================

    @GetMapping("/api/categories")
    @ResponseBody
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(danhMucService.getAll());
    }

    @PostMapping("/categories/add")
    @ResponseBody
    public ResponseEntity<?> addCategory(@ModelAttribute DanhMuc danhMuc) {
        Map<String, String> res = new HashMap<>();

        if (danhMucService.existsByName(danhMuc.getTenDanhMuc())) {
            res.put("status", "error");
            res.put("message", "Danh mục đã tồn tại");
            return ResponseEntity.badRequest().body(res);
        }

        danhMucService.save(danhMuc);
        res.put("status", "success");
        res.put("message", "Thêm danh mục thành công");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/categories/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        danhMucService.delete(id);
        Map<String, String> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Xóa danh mục thành công");
        return ResponseEntity.ok(res);
    }

    // ================== SPORT API ==================

    @GetMapping("/api/sports")
    @ResponseBody
    public ResponseEntity<?> getAllSports() {
        return ResponseEntity.ok(monTheThaoService.getAll());
    }

    @PostMapping("/sports/add")
    @ResponseBody
    public ResponseEntity<?> addSport(@ModelAttribute MonTheThao monTheThao) {
        Map<String, String> res = new HashMap<>();

        if (monTheThaoService.existsByName(monTheThao.getTenMonTheThao())) {
            res.put("status", "error");
            res.put("message", "Môn thể thao đã tồn tại");
            return ResponseEntity.badRequest().body(res);
        }

        monTheThaoService.save(monTheThao);
        res.put("status", "success");
        res.put("message", "Thêm môn thể thao thành công");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/sports/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteSport(@PathVariable Integer id) {
        monTheThaoService.delete(id);
        Map<String, String> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Xóa môn thể thao thành công");
        return ResponseEntity.ok(res);
    }
}
