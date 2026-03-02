package com.apartment.controller;

import com.apartment.dto.LoginDto;
import com.apartment.dto.RegisterDto;
import com.apartment.model.Users;
import com.apartment.repository.UsersRepository;
import com.apartment.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý các request liên quan đến xác thực:
 * - GET /login → hiển thị form đăng nhập
 * - POST /login → xử lý đăng nhập
 * - GET /register → hiển thị form đăng ký
 * - POST /register → xử lý đăng ký tài khoản mới
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository usersRepository; // Dùng để kiểm tra tài khoản bị khoá

    // ===== UC-01: ĐĂNG NHẬP =====

    /**
     * Hiển thị trang đăng nhập.
     * Nếu đã login rồi → chuyển về /profile
     */
    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {
        // Nếu đã đăng nhập thì không cho vào trang login nữa
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/profile";
        }
        model.addAttribute("loginForm", new LoginDto());
        return "auth/login";
    }

    /**
     * Xử lý đăng nhập khi user submit form.
     * Luồng: nhập form → authenticate → session → redirect
     */
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("loginForm") LoginDto loginForm,
            HttpSession session,
            Model model) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        // Kiểm tra field trống
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu");
            model.addAttribute("loginForm", loginForm);
            return "auth/login";
        }

        try {
            // Gọi service xác thực username + password
            Users user = userService.authenticate(username.trim(), password);

            if (user == null) {
                // Sai thông tin đăng nhập (username không tồn tại hoặc sai password)
                model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
                model.addAttribute("loginForm", loginForm);
                return "auth/login";
            }

            // Đăng nhập thành công: Lưu user vào session
            session.setAttribute("loggedUser", user);
            return "redirect:/home";

        } catch (RuntimeException e) {
            if ("ACCOUNT_LOCKED".equals(e.getMessage())) {
                model.addAttribute("error", "Tài khoản đã bị khoá. Vui lòng liên hệ quản trị viên.");
            } else {
                model.addAttribute("error", "Có lỗi xảy ra, vui lòng thử lại");
            }
            model.addAttribute("loginForm", loginForm);
            return "auth/login";
        }
    }

    /**
     * Đăng xuất: xóa session và redirect về trang login.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa toàn bộ session
        return "redirect:/login?logout";
    }

    // ===== UC-02: ĐĂNG KÝ =====

    /**
     * Hiển thị trang đăng ký tài khoản mới.
     */
    @GetMapping("/register")
    public String showRegisterForm(HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/profile";
        }
        model.addAttribute("registerForm", new RegisterDto());
        return "auth/register";
    }

    /**
     * Xử lý đăng ký tài khoản mới.
     * Kiểm tra validation → kiểm tra trùng username/email → tạo tài khoản
     */
    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("registerForm") RegisterDto registerForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra validation annotations (@NotBlank, @Size, @Email...)
        if (bindingResult.hasErrors()) {
            return "auth/register"; // Trả về form với thông báo lỗi
        }

        // Kiểm tra confirmPassword có khớp password không
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword",
                    "Xác nhận mật khẩu không khớp");
            return "auth/register";
        }

        // Kiểm tra username đã tồn tại chưa
        if (userService.isUsernameTaken(registerForm.getUsername())) {
            bindingResult.rejectValue("username", "error.username",
                    "Tên đăng nhập đã được sử dụng");
            return "auth/register";
        }

        // Kiểm tra email đã tồn tại chưa
        if (usersRepository.existsByEmail(registerForm.getEmail())) {
            bindingResult.rejectValue("email", "error.email",
                    "Email đã được sử dụng bởi tài khoản khác");
            return "auth/register";
        }

        // Tất cả hợp lệ → tạo tài khoản mới
        userService.register(registerForm);

        // Redirect về login với flash message thành công
        redirectAttributes.addFlashAttribute("successMessage",
                "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}
