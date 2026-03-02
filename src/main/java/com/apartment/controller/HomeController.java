package com.apartment.controller;

import com.apartment.model.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller xử lý trang chủ (Dashboard) sau khi đăng nhập.
 */
@Controller
public class HomeController {

    @GetMapping({ "/", "/home" })
    public String home(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "home/index";
    }
}
