package com.apartment.service.impl;

import com.apartment.model.Role;
import com.apartment.model.Staff;
import com.apartment.model.Users;
import com.apartment.repository.RoleRepository;
import com.apartment.repository.StaffRepository;
import com.apartment.repository.UsersRepository;
import com.apartment.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    @Transactional
    public void assignRoleToStaff(Integer staffId, Integer roleId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        Users user = staff.getUser();
        user.setRole(role);
        usersRepository.save(user);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
