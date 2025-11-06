package com.sportshop.sports_shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sportshop.sports_shop.model.ThuongHieu;
import com.sportshop.sports_shop.service.ThuongHieuService;

@RestController
@RequestMapping("/api/thuonghieu")
@CrossOrigin("*") // Cho phép gọi từ HTML/JS
public class ThuongHieuController {

    @Autowired
    private ThuongHieuService thuongHieuService;

    // API lấy toàn bộ thương hiệu
    @GetMapping("/all")
    public List<ThuongHieu> getAllThuongHieu() {
        return thuongHieuService.getAll();
    }
}
