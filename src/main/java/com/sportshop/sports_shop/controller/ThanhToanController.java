package com.sportshop.sports_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThanhToanController {

    @GetMapping("/payment")
    public String thanhToanPage() {
        return "client/home/payment"; 
    }
}
