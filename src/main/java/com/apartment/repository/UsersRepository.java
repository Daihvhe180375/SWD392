package com.apartment.repository;

import com.apartment.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository truy vấn bảng Users trong database.
 * Spring Data JPA tự sinh các câu SQL từ tên method.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    // Tìm user theo username (dùng cho đăng nhập)
    Optional<Users> findByUsername(String username);

    // Tìm user theo email (dùng kiểm tra trùng email khi đăng ký)
    Optional<Users> findByEmail(String email);

    // Kiểm tra username đã tồn tại chưa
    boolean existsByUsername(String username);

    // Kiểm tra email đã tồn tại chưa (loại trừ chính user đang sửa)
    boolean existsByEmailAndUserIdNot(String email, Integer userId);

    // Kiểm tra email đã tồn tại chưa (dùng khi đăng ký user mới)
    boolean existsByEmail(String email);
}
