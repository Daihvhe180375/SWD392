package com.apartment.service.impl;

import com.apartment.dto.EditProfileDto;
import com.apartment.dto.RegisterDto;
import com.apartment.model.Resident;
import com.apartment.model.Role;
import com.apartment.model.Users;
import com.apartment.repository.ResidentRepository;
import com.apartment.repository.UsersRepository;
import com.apartment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Cài đặt cụ thể của UserService - toàn bộ nghiệp vụ xử lý tài khoản.
 * Controller sẽ gọi UserService interface, không biết class này tồn tại.
 * Nguyên tắc OOP: Single Responsibility - class này chỉ xử lý logic user.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ResidentRepository residentRepository;

    /**
     * Xác thực đăng nhập: tìm user theo username, so sánh password plain text.
     * Kiểm tra thêm trạng thái is_active của tài khoản.
     *
     * @throws RuntimeException nếu tài khoản bị khoá (để AuthController phân biệt
     *                          với sai pass)
     */
    @Override
    @Transactional
    public Users authenticate(String username, String password) {
        // Bước 1: Tìm user theo username trong DB
        Users user = usersRepository.findByUsername(username).orElse(null);

        // Bước 2: Không tìm thấy user → trả null (sai username)
        if (user == null) {
            return null;
        }

        // Bước 3: Kiểm tra tài khoản bị khoá TRƯỚC (để hiện đúng thông báo)
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new RuntimeException("ACCOUNT_LOCKED");
        }

        // Bước 4: So sánh password plain text
        String storedPassword = user.getPassword();
        if (storedPassword == null || !storedPassword.trim().equals(password.trim())) {
            return null; // Sai mật khẩu
        }

        // Bước 5: Đăng nhập thành công → cập nhật thời gian đăng nhập gần nhất
        try {
            user.setLastLogin(LocalDateTime.now());
            usersRepository.save(user);
        } catch (Exception e) {
            // Không crash login nếu update lastLogin bị lỗi
        }

        return user;
    }

    /**
     * Kiểm tra tài khoản có bị khoá không (dùng để phân biệt lỗi đăng nhập).
     */
    public boolean isAccountLocked(String username) {
        return usersRepository.findByUsername(username)
                .map(u -> !u.getIsActive())
                .orElse(false);
    }

    /**
     * Đăng ký tài khoản mới:
     * 1. Hash password bằng BCrypt
     * 2. Set role = Resident (role_id = 3)
     * 3. Lưu vào bảng Users
     * 4. Tạo bản ghi Resident liên kết với user vừa tạo
     */
    @Override
    @Transactional
    public void register(RegisterDto dto) {
        // Tạo đối tượng Users mới
        Users newUser = new Users();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());

        // Lưu password dạng plain text (không mã hoá)
        newUser.setPassword(dto.getPassword());

        newUser.setFullName(dto.getFullName());
        newUser.setPhone(dto.getPhone());
        newUser.setIsActive(true);
        newUser.setCreatedDate(LocalDateTime.now());

        // Set role mặc định là Resident (role_id = 3)
        Role residentRole = new Role();
        residentRole.setRoleId(3); // Giá trị cố định theo yêu cầu
        newUser.setRole(residentRole);

        // Lưu user vào DB
        Users savedUser = usersRepository.save(newUser);

        // Tạo bản ghi Resident liên kết với user vừa tạo
        Resident newResident = new Resident();
        newResident.setUser(savedUser);
        residentRepository.save(newResident);
    }

    /**
     * Lấy thông tin user mới nhất từ DB theo userId.
     */
    @Override
    public Users findById(Integer userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    /**
     * Cập nhật thông tin hồ sơ cá nhân (fullName, email, phone, avatar).
     * Không cho sửa username, role, password.
     */
    @Override
    @Transactional
    public void updateProfile(Integer userId, EditProfileDto dto) {
        // Lấy user hiện tại từ DB
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user ID: " + userId));

        // Cập nhật các trường được phép sửa
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        // Avatar: nếu để trống thì giữ nguyên giá trị cũ
        if (dto.getAvatar() != null && !dto.getAvatar().trim().isEmpty()) {
            user.setAvatar(dto.getAvatar().trim());
        }

        // Lưu lại vào DB
        usersRepository.save(user);
    }

    /**
     * Kiểm tra email đã bị dùng bởi user khác chưa.
     * excludeUserId: ID của user đang sửa - không tính vào kết quả
     */
    @Override
    public boolean isEmailTaken(String email, Integer excludeUserId) {
        return usersRepository.existsByEmailAndUserIdNot(email, excludeUserId);
    }

    /**
     * Kiểm tra username đã tồn tại chưa (dùng khi đăng ký).
     */
    @Override
    public boolean isUsernameTaken(String username) {
        return usersRepository.existsByUsername(username);
    }
}
