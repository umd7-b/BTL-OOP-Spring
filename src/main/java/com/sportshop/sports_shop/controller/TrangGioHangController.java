package com.sportshop.sports_shop.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller


public class TrangGioHangController {
    @GetMapping("/cart")
    public String cartPage() {
        return "/client/home/cart"; 
    }
}
