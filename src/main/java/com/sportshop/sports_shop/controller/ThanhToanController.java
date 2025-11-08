package com.sportshop.sports_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThanhToanController {

    @GetMapping("/thanhtoan")
    public String thanhToanPage() {
        return "client/home/payment"; 
    }
}
