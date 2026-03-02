package com.apartment.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO nhận dữ liệu form chỉnh sửa hồ sơ cá nhân.
 * Không cho sửa username, role, password (có form riêng).
 */
@Getter
@Setter
public class EditProfileDto {

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 150, message = "Họ và tên phải từ 2 đến 150 ký tự")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Pattern(regexp = "([0-9]{9,11})?", message = "Số điện thoại phải từ 9 đến 11 chữ số")
    private String phone;

    // Avatar URL - có thể để trống (sẽ dùng ảnh mặc định)
    private String avatar;
}
