package com.apartment.repository;

import com.apartment.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository truy vấn bảng Resident.
 */
@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {

    // Tìm thông tin Resident theo user_id (dùng trong View Profile)
    Optional<Resident> findByUserUserId(Integer userId);
}
