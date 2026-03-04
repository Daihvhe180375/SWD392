package com.apartment.repository;

import com.apartment.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository truy vấn bảng Role.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
