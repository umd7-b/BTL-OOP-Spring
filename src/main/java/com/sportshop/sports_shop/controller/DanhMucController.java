package com.sportshop.sports_shop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sportshop.sports_shop.model.DanhMuc;
import com.sportshop.sports_shop.service.DanhMucService;

@RestController
@RequestMapping("/api/danhmuc")
@CrossOrigin(origins = "http://localhost:8081") // cho phép frontend gọi
public class DanhMucController {

    private final DanhMucService danhMucService;

    public DanhMucController(DanhMucService danhMucService) {
        this.danhMucService = danhMucService;
    }

    @GetMapping("/all")
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucService.getAll();
    }
}
