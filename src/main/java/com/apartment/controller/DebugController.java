package com.apartment.controller;

import com.apartment.repository.UsersRepository;
import com.apartment.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller debug TẠM THỜI - dùng để setup data.
 * XÓA class này sau khi đã setup xong!
 */
@RestController
public class DebugController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    /**
     * Endpoint: /debug/reset-passwords?pass=password123
     * Reset password cho tất cả user test.
     */
    @GetMapping("/debug/reset-passwords")
    public Map<String, Object> resetPasswords(@RequestParam(defaultValue = "password123") String pass) {
        String hash = passwordEncoder.encode(pass);

        List<Users> allUsers = usersRepository.findAll();
        for (Users u : allUsers) {
            u.setPassword(hash);
        }
        usersRepository.saveAll(allUsers);

        Map<String, Object> result = new HashMap<>();
        result.put("hash", hash);
        result.put("updatedUsers", allUsers.stream().map(Users::getUsername).toList());
        result.put("message", "Password reset thành công cho tất cả users. Mật khẩu mới: " + pass);
        return result;
    }

    /**
     * Endpoint: /debug/hash?text=password123
     * Generate BCrypt hash cho một chuỗi bất kỳ.
     */
    @GetMapping("/debug/hash")
    public Map<String, String> generateHash(@RequestParam String text) {
        Map<String, String> result = new HashMap<>();
        result.put("text", text);
        result.put("hash", passwordEncoder.encode(text));
        return result;
    }
}
