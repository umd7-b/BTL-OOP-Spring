package com.sportshop.sports_shop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sportshop.sports_shop.model.MonTheThao;
import com.sportshop.sports_shop.service.MonTheThaoService;

@RestController
@RequestMapping("/api/monthethao")
@CrossOrigin(origins = "http://localhost:8081") // cho phép gọi từ frontend (port 8081)
public class MonTheThaoController {

    private final MonTheThaoService monTheThaoService;

    public MonTheThaoController(MonTheThaoService monTheThaoService) {
        this.monTheThaoService = monTheThaoService;
    }

    @GetMapping("/all")
    public List<MonTheThao> getAll() {
        return monTheThaoService.getAll();
    }
}
