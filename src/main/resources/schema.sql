-- =====================================================
-- ApartmentManagement Database Schema
-- SQL Server Script
-- Chạy script này để tạo database và dữ liệu mẫu
-- =====================================================

-- Tạo database nếu chưa có
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'ApartmentManagement')
BEGIN
    CREATE DATABASE ApartmentManagement;
END
GO

USE ApartmentManagement;
GO

-- =====================================================
-- Bảng Role: phân quyền người dùng
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Role')
BEGIN
    CREATE TABLE Role (
        role_id   INT IDENTITY(1,1) PRIMARY KEY,
        role_name NVARCHAR(50) NOT NULL UNIQUE  -- Admin / Staff / Resident / Manager
    );
END
GO

-- =====================================================
-- Bảng Users: tài khoản đăng nhập
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Users')
BEGIN
    CREATE TABLE Users (
        user_id      INT IDENTITY(1,1) PRIMARY KEY,
        username     NVARCHAR(50)  NOT NULL UNIQUE,
        password     NVARCHAR(255) NOT NULL,          -- plain text để test dễ dàng
        email        NVARCHAR(100) NOT NULL UNIQUE,
        full_name    NVARCHAR(150) NOT NULL,
        phone        NVARCHAR(15),
        avatar       NVARCHAR(500),                   -- URL ảnh đại diện
        is_active    BIT NOT NULL DEFAULT 1,          -- 1 = active, 0 = bị khoá
        created_date DATETIME2 DEFAULT GETDATE(),
        last_login   DATETIME2,
        role_id      INT NOT NULL,
        CONSTRAINT FK_Users_Role FOREIGN KEY (role_id) REFERENCES Role(role_id)
    );
END
GO

-- =====================================================
-- Bảng Staff: thông tin nhân viên
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Staff')
BEGIN
    CREATE TABLE Staff (
        staff_id  INT IDENTITY(1,1) PRIMARY KEY,
        hire_date DATE,
        user_id   INT NOT NULL UNIQUE,
        CONSTRAINT FK_Staff_Users FOREIGN KEY (user_id) REFERENCES Users(user_id)
    );
END
GO

-- =====================================================
-- Bảng Resident: thông tin cư dân
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Resident')
BEGIN
    CREATE TABLE Resident (
        resident_id   INT IDENTITY(1,1) PRIMARY KEY,
        id_card       NVARCHAR(20),                   -- Số CMND / CCCD
        move_in_date  DATE,
        apartment_id  NVARCHAR(20),                   -- Số phòng
        user_id       INT NOT NULL UNIQUE,
        CONSTRAINT FK_Resident_Users FOREIGN KEY (user_id) REFERENCES Users(user_id)
    );
END
GO

-- =====================================================
-- Dữ liệu mẫu: 4 Role
-- =====================================================
IF NOT EXISTS (SELECT * FROM Role)
BEGIN
    INSERT INTO Role (role_name) VALUES
    (N'Admin'),
    (N'Staff'),
    (N'Resident'),
    (N'Manager');
END
GO

-- =====================================================
-- Dữ liệu mẫu: Users (password = 'password123' plain text)
-- =====================================================
IF NOT EXISTS (SELECT * FROM Users WHERE username = 'admin')
BEGIN
    INSERT INTO Users (username, password, email, full_name, phone, avatar, is_active, role_id) VALUES
    -- Role Admin (role_id=1)
    ('admin',
     'password123',
     'admin@apartment.com',
     N'Quản Trị Viên',
     '0901234567',
     'https://ui-avatars.com/api/?name=Admin&background=dc3545&color=fff',
     1, 1),

    -- Role Staff (role_id=2)
    ('staff01',
     'password123',
     'staff01@apartment.com',
     N'Nguyễn Văn Bình',
     '0912345678',
     'https://ui-avatars.com/api/?name=Binh+Nguyen&background=0d6efd&color=fff',
     1, 2),

    -- Role Resident (role_id=3)
    ('resident01',
     'password123',
     'resident01@apartment.com',
     N'Trần Thị Lan',
     '0923456789',
     'https://ui-avatars.com/api/?name=Lan+Tran&background=198754&color=fff',
     1, 3),

    -- Role Manager (role_id=4)
    ('manager01',
     'password123',
     'manager01@apartment.com',
     N'Lê Quốc Hùng',
     '0934567890',
     'https://ui-avatars.com/api/?name=Hung+Le&background=6f42c1&color=fff',
     1, 4),

    -- Tài khoản bị khoá (để test UC-01)
    ('locked_user',
     'password123',
     'locked@apartment.com',
     N'Tải Khoản Bị Khoá',
     '0945678901',
     NULL,
     0, 3);  -- is_active = 0
END
GO

-- =====================================================
-- Dữ liệu mẫu: Staff (liên kết với staff01 - user_id=2)
-- =====================================================
IF NOT EXISTS (SELECT * FROM Staff)
BEGIN
    INSERT INTO Staff (hire_date, user_id)
    SELECT '2023-06-15', user_id FROM Users WHERE username = 'staff01';
END
GO

-- =====================================================
-- Dữ liệu mẫu: Resident (liên kết với resident01 - user_id=3)
-- =====================================================
IF NOT EXISTS (SELECT * FROM Resident)
BEGIN
    INSERT INTO Resident (id_card, move_in_date, apartment_id, user_id)
    SELECT '079012345678', '2024-01-01', N'A101', user_id FROM Users WHERE username = 'resident01';

    -- Resident cho locked_user
    INSERT INTO Resident (id_card, move_in_date, apartment_id, user_id)
    SELECT '079099999999', '2024-06-01', N'B205', user_id FROM Users WHERE username = 'locked_user';
END
GO

PRINT N'Schema và dữ liệu mẫu đã được tạo thành công!';
PRINT N'Tài khoản test (password: password123):';
PRINT N'  admin      → role: Admin';
PRINT N'  staff01    → role: Staff';
PRINT N'  resident01 → role: Resident';
PRINT N'  manager01  → role: Manager';
PRINT N'  locked_user → bị khoá (is_active=0)';
GO
