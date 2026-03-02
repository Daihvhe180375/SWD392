package com.apartment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity ánh xạ bảng Staff - lưu thông tin nhân viên.
 * Liên kết 1-1 với bảng Users qua user_id.
 */
@Entity
@Table(name = "Staff")
@Getter
@Setter
@NoArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "hire_date")
    private LocalDate hireDate; // Ngày tuyển dụng

    // Quan hệ ManyToOne: một nhân viên liên kết với một user
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;
}
