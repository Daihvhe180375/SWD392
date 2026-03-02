package com.apartment.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO nhận dữ liệu form đăng nhập.
 */
@Getter
@Setter
public class LoginDto {

    private String username; // Tên đăng nhập
    private String password; // Mật khẩu
}
