package com.sportshop.sports_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ⚠️ Tắt CSRF để cho phép fetch POST JSON
            .csrf(csrf -> csrf.disable())

            // Cho phép truy cập tất cả (hoặc bạn có thể giới hạn nếu muốn)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/sanpham/**").permitAll()
                .requestMatchers(
                    "/uploads/**", // <<< CHO PHÉP TRUY CẬP THƯ MỤC UPLOADS
                    "/client/**", 
                    "/css/**", 
                    "/js/**"
                ).permitAll()
                .requestMatchers("/register", "/login", "/client/**", "/js/**", "/css/**").permitAll()
                .anyRequest().permitAll()
            )

            // Vô hiệu hóa form login mặc định của Spring
            .formLogin(form -> form.disable())

            // Cho phép logout
            
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}
