package com.apartment.controller;

import com.apartment.model.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller xử lý giao diện cho Admin/Manager.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/staff")
    public String staffManagement(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loggedUser");

        // Kiểm tra đăng nhập và quyền (Admin hoặc Manager)
        if (user == null) {
            return "redirect:/login";
        }

        String role = user.getRole().getRoleName();
        if (!"Admin".equals(role) && !"Manager".equals(role)) {
            return "redirect:/home"; // Không đủ quyền thì về home
        }

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Quản lý nhân viên");
        return "admin/staff"; // → templates/admin/staff.html
    }
}
