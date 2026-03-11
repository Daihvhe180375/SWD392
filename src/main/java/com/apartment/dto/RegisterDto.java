package com.apartment.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO nhận dữ liệu form đăng ký tài khoản mới.
 * Dùng @Valid để kích hoạt kiểm tra validation tự động.
 */
@Getter
@Setter
public class  RegisterDto {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Tên đăng nhập chỉ được chứa chữ, số và dấu gạch dưới")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    private String password;

    // confirmPassword được kiểm tra khớp trong Service
    private String confirmPassword;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 150, message = "Họ và tên phải từ 2 đến 150 ký tự")
    private String fullName;

    @Pattern(regexp = "(^$|[0-9]{9,11})", message = "Số điện thoại phải từ 9 đến 11 chữ số")
    private String phone;
}
