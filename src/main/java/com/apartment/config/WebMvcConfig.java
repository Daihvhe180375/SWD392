package com.apartment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình Spring MVC - đăng ký các Interceptor.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // Chỉ áp dụng kiểm tra đăng nhập cho các URL này
                .addPathPatterns("/", "/home", "/profile", "/profile/**")
                // Loại trừ các trang public (không cần đăng nhập)
                .excludePathPatterns("/login", "/register", "/logout",
                        "/css/**", "/js/**", "/images/**",
                        "/debug/**");
    }
}
