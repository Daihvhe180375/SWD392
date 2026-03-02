package com.apartment.service;

import com.apartment.model.Role;

import java.util.List;

public interface RoleService {
    void assignRoleToStaff(Integer staffId, Integer roleId);

    List<Role> getAllRoles();
}
