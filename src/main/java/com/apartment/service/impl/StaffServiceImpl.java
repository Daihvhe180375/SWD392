package com.apartment.service.impl;

import com.apartment.dto.StaffCreateDto;
import com.apartment.model.Role;
import com.apartment.model.Staff;
import com.apartment.model.Users;
import com.apartment.repository.RoleRepository;
import com.apartment.repository.StaffRepository;
import com.apartment.repository.UsersRepository;
import com.apartment.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void addStaff(StaffCreateDto dto) {
        if (usersRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        if (usersRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Giữ nguyên plain text theo trạng thái hiện tại của dự án
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setIsActive(true);
        user.setCreatedDate(LocalDateTime.now());
        user.setRole(role);

        Users savedUser = usersRepository.save(user);

        Staff staff = new Staff();
        staff.setUser(savedUser);
        staff.setHireDate(dto.getHireDate());

        staffRepository.save(staff);
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public java.util.Optional<Staff> findByUserId(Integer userId) {
        return staffRepository.findByUserUserId(userId);
    }
}
