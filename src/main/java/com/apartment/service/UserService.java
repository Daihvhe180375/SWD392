package com.apartment.service;

import com.apartment.dto.EditProfileDto;
import com.apartment.dto.RegisterDto;
import com.apartment.model.Users;

/**
 * Interface định nghĩa các hành vi (nghiệp vụ) liên quan đến tài khoản người
 * dùng.
 * Controller chỉ gọi interface này, không biết cài đặt bên trong.
 * Nguyên tắc OOP: Abstraction - không phụ thuộc vào cài đặt cụ thể.
 */
public interface UserService {

    /**
     * Xác thực đăng nhập: kiểm tra username/password có đúng không.
     * 
     * @return Users nếu đúng, null nếu sai hoặc tài khoản bị khoá
     */
    Users authenticate(String username, String password);

    /**
     * Đăng ký tài khoản mới với role Resident.
     * Tự động hash password và tạo bản ghi Resident liên kết.
     */
    void register(RegisterDto dto);

    /**
     * Lấy thông tin user theo ID (truy vấn mới nhất từ DB).
     */
    Users findById(Integer userId);

    /**
     * Cập nhật thông tin hồ sơ cá nhân (fullName, email, phone, avatar).
     */
    void updateProfile(Integer userId, EditProfileDto dto);

    /**
     * Kiểm tra email đã bị dùng bởi user khác chưa.
     * 
     * @param excludeUserId ID user cần loại trừ (chính user đang sửa)
     */
    boolean isEmailTaken(String email, Integer excludeUserId);

    /**
     * Kiểm tra username đã tồn tại trong DB chưa.
     */
    boolean isUsernameTaken(String username);
}
