package com.apartment.repository;

import com.apartment.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository truy vấn bảng Staff.
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    // Tìm thông tin Staff theo user_id (dùng trong View Profile)
    Optional<Staff> findByUserUserId(Integer userId);
}
