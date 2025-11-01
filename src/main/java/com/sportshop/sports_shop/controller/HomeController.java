package com.sportshop.sports_shop.controller; 

import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping; 

@Controller 
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "client/home/index"; // Sẽ trả về file "index.html" trong thư mục "templates"
    }
}