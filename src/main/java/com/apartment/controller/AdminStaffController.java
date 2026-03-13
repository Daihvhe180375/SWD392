package com.apartment.controller;

import com.apartment.dto.StaffCreateDto;
import com.apartment.dto.StaffResponseDto;
import com.apartment.model.Role;
import com.apartment.model.Staff;
import com.apartment.service.RoleService;
import com.apartment.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminStaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private RoleService roleService;

    // UC-ADMIN-01: Thêm nhân viên mới
    @PostMapping("/staff")
    public ResponseEntity<?> addStaff(@Valid @RequestBody StaffCreateDto dto) {
        try {
            staffService.addStaff(dto);
            return ResponseEntity.ok(Map.of("message", "Thêm nhân viên thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // UC-ADMIN-02: Gán role cho nhân viên (Staff <-> Manager)
    @PutMapping("/staff/{staffId}/role")
    public ResponseEntity<?> assignRole(@PathVariable Integer staffId, @RequestBody Map<String, Integer> payload) {
        try {
            Integer roleId = payload.get("roleId");
            if (roleId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "roleId không được để trống"));
            }
            roleService.assignRoleToStaff(staffId, roleId);
            return ResponseEntity.ok(Map.of("message", "Cập nhật vai trò thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // UC-ADMIN-03: Lấy danh sách Role
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Lấy danh sách tất cả nhân viên - trả về DTO để tránh lỗi lazy-load JSON
    @GetMapping("/staff")
    public ResponseEntity<List<StaffResponseDto>> getAllStaff() {
        List<StaffResponseDto> result = staffService.getAllStaff()
                .stream()
                .map(StaffResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
