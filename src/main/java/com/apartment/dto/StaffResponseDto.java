package com.apartment.dto;

import com.apartment.model.Staff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO dùng để trả về thông tin nhân viên qua API JSON.
 * Tránh lỗi lazy-load và vòng lặp khi Jackson serialize entity Staff trực tiếp.
 */
@Getter
@Setter
@NoArgsConstructor
public class StaffResponseDto {
    private Integer staffId;
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private LocalDate hireDate;
    private RoleDto role;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RoleDto {
        private Integer roleId;
        private String roleName;
    }

    /**
     * Chuyển đổi từ Staff entity sang DTO.
     */
    public static StaffResponseDto from(Staff staff) {
        StaffResponseDto dto = new StaffResponseDto();
        dto.setStaffId(staff.getStaffId());
        dto.setHireDate(staff.getHireDate());

        if (staff.getUser() != null) {
            dto.setUserId(staff.getUser().getUserId());
            dto.setUsername(staff.getUser().getUsername());
            dto.setFullName(staff.getUser().getFullName());
            dto.setEmail(staff.getUser().getEmail());
            dto.setPhone(staff.getUser().getPhone());
            dto.setAvatar(staff.getUser().getAvatar());

            if (staff.getUser().getRole() != null) {
                RoleDto roleDto = new RoleDto();
                roleDto.setRoleId(staff.getUser().getRole().getRoleId());
                roleDto.setRoleName(staff.getUser().getRole().getRoleName());
                dto.setRole(roleDto);
            }
        }
        return dto;
    }
}
