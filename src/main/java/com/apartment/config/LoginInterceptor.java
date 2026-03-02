package com.apartment.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor kiểm tra xem người dùng đã đăng nhập chưa.
 * Nếu chưa đăng nhập → redirect về /login.
 *
 * Cách hoạt động: sau khi login thành công, AuthController lưu thông tin user
 * vào session với key "loggedUser". Interceptor này kiểm tra key đó.
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        // Kiểm tra session có tồn tại và có chứa loggedUser không
        boolean isLoggedIn = (session != null && session.getAttribute("loggedUser") != null);

        if (!isLoggedIn) {
            // Chưa đăng nhập → chuyển về trang login
            response.sendRedirect(request.getContextPath() + "/login");
            return false; // Dừng xử lý tiếp
        }

        return true; // Đã đăng nhập → tiếp tục xử lý request
    }
}
