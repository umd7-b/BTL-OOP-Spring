package com.sportshop.sports_shop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sportshop.sports_shop.model.SanPham;
import com.sportshop.sports_shop.service.SanPhamService;

@RestController
@RequestMapping("/api/sanpham")
@CrossOrigin(origins = "http://localhost:8081") // Cho phép frontend gọi
public class SanPhamController {

    private final SanPhamService sanPhamService;

    public SanPhamController(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
    }

    // --- Lấy toàn bộ sản phẩm ---
    @GetMapping("/all")
    public List<SanPham> getAllSanPham() {
        return sanPhamService.getAll();
    }

    // --- Lấy sản phẩm theo ID ---
    @GetMapping("/{id}")
    public Optional<SanPham> getById(@PathVariable Long id) {
        return sanPhamService.getById(id);
    }

    // --- Tìm kiếm sản phẩm theo tên ---
    @GetMapping("/search")
    public List<SanPham> search(@RequestParam String keyword) {
        return sanPhamService.search(keyword);
    }

    // --- Thêm sản phẩm ---
    @PostMapping
    public SanPham addSanPham(@RequestBody SanPham sanPham) {
        return sanPhamService.save(sanPham);
    }

    // --- Cập nhật sản phẩm ---
    @PutMapping("/{id}")
    public SanPham updateSanPham(@PathVariable Long id, @RequestBody SanPham sanPham) {
        sanPham.setMaSp(id.intValue());
        return sanPhamService.save(sanPham);
    }

    // --- Xóa sản phẩm ---
    @DeleteMapping("/{id}")
    public void deleteSanPham(@PathVariable Long id) {
        sanPhamService.delete(id);
    }
}
