package com.apartment.controller;

import com.apartment.dto.EditProfileDto;
import com.apartment.model.Resident;
import com.apartment.model.Staff;
import com.apartment.model.Users;
import com.apartment.service.ResidentService;
import com.apartment.service.StaffService;
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

import java.util.Optional;

/**
 * Controller xử lý các request liên quan đến hồ sơ người dùng:
 * - GET /profile → xem hồ sơ
 * - GET /profile/edit → form chỉnh sửa hồ sơ
 * - POST /profile/edit → lưu thay đổi
 */
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private StaffService staffService;

    // ===== UC-03: XEM HỒ SƠ =====

    /**
     * Hiển thị trang hồ sơ cá nhân.
     * Tải thêm thông tin Resident hoặc Staff nếu user có role tương ứng.
     */
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        // Lấy user hiện tại từ session
        Users currentUser = (Users) session.getAttribute("loggedUser");

        // Nếu chưa đăng nhập → redirect về login
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Lấy thông tin user mới nhất từ DB (có thể vừa được cập nhật)
        Users user = userService.findById(currentUser.getUserId());
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // Cập nhật session với thông tin mới nhất
        session.setAttribute("loggedUser", user);

        // Đưa thông tin user vào model để Thymeleaf render
        model.addAttribute("user", user);

        String roleName = user.getRole().getRoleName();

        // Nếu là Resident → tải thêm thông tin căn hộ
        if ("Resident".equals(roleName)) {
            Optional<Resident> resident = residentService.findByUserId(user.getUserId());
            resident.ifPresent(r -> model.addAttribute("resident", r));
        }

        // Nếu là Staff → tải thêm ngày tuyển dụng
        if ("Staff".equals(roleName)) {
            Optional<Staff> staff = staffService.findByUserId(user.getUserId());
            staff.ifPresent(s -> model.addAttribute("staff", s));
        }

        return "profile/view"; // → templates/profile/view.html
    }

    // ===== UC-04: CHỈNH SỬA HỒ SƠ =====

    /**
     * Hiển thị form chỉnh sửa hồ sơ, điền sẵn thông tin hiện tại.
     */
    @GetMapping("/profile/edit")
    public String showEditForm(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Lấy thông tin mới nhất từ DB
        Users user = userService.findById(currentUser.getUserId());

        // Tạo DTO và điền sẵn giá trị hiện tại vào form
        EditProfileDto editDto = new EditProfileDto();
        editDto.setFullName(user.getFullName());
        editDto.setEmail(user.getEmail());
        editDto.setPhone(user.getPhone());
        editDto.setAvatar(user.getAvatar());

        model.addAttribute("user", user);
        model.addAttribute("editDto", editDto);

        return "profile/edit"; // → templates/profile/edit.html
    }

    /**
     * Xử lý lưu thay đổi hồ sơ.
     * Validate → kiểm tra email unique → update DB → redirect /profile
     */
    @PostMapping("/profile/edit")
    public String handleEditProfile(@Valid @ModelAttribute("editDto") EditProfileDto editDto,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Users currentUser = (Users) session.getAttribute("loggedUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Lấy thông tin user để truyền vào view nếu có lỗi
        Users user = userService.findById(currentUser.getUserId());
        model.addAttribute("user", user);

        // Kiểm tra validation annotations
        if (bindingResult.hasErrors()) {
            return "profile/edit";
        }

        // Kiểm tra email mới có bị dùng bởi user khác không
        if (userService.isEmailTaken(editDto.getEmail(), currentUser.getUserId())) {
            bindingResult.rejectValue("email", "error.email",
                    "Email này đã được sử dụng bởi tài khoản khác");
            return "profile/edit";
        }

        // Tất cả hợp lệ → cập nhật thông tin
        userService.updateProfile(currentUser.getUserId(), editDto);

        // Cập nhật lại session với thông tin mới
        Users updatedUser = userService.findById(currentUser.getUserId());
        session.setAttribute("loggedUser", updatedUser);

        // Redirect về /profile với flash message thành công
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        return "redirect:/profile";
    }
}
