package com.apartment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cấu hình Spring Security.
 *
 * NOTE: Chúng ta dùng LoginInterceptor (session-based) để bảo vệ URL,
 * không dùng Spring Security URL authorization để tránh xung đột.
 * Spring Security ở đây chỉ dùng để:
 * - Cung cấp BCryptPasswordEncoder bean
 * - Tắt các tính năng mặc định không cần thiết
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean BCryptPasswordEncoder - dùng để hash và verify password.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình Security Filter Chain.
     * Permit tất cả request - việc kiểm tra đăng nhập do LoginInterceptor đảm
     * nhiệm.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cho phép TẤT CẢ request đi qua Spring Security
                // (LoginInterceptor sẽ tự kiểm tra session)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                // Tắt CSRF (form Thymeleaf không dùng CSRF token)
                .csrf(csrf -> csrf.disable())
                // Tắt form login mặc định của Spring Security
                .formLogin(form -> form.disable())
                // Tắt HTTP Basic Auth
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
