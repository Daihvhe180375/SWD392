package com.apartment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity ánh xạ bảng Resident - lưu thông tin cư dân sống tại chung cư.
 * Liên kết 1-1 với bảng Users qua user_id.
 */
@Entity
@Table(name = "Resident")
@Getter
@Setter
@NoArgsConstructor
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resident_id")
    private Integer residentId;

    @Column(name = "id_card", length = 20)
    private String idCard; // Số CMND / CCCD

    @Column(name = "move_in_date")
    private LocalDate moveInDate; // Ngày chuyển vào

    @Column(name = "apartment_id", length = 20)
    private String apartmentId; // Mã số phòng (VD: A101, B205)

    // Quan hệ 1-1: một cư dân liên kết với một tài khoản user
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;
}
