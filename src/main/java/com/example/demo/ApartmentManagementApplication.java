package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Điểm khởi chạy ứng dụng ApartmentManagement.
 * - scanBasePackages: quét @Service, @Controller, @Component ở package
 * com.apartment
 * - @EnableJpaRepositories: chỉ Spring Data JPA quét đúng package chứa
 * Repository
 * - @EntityScan: chỉ Hibernate quét đúng package chứa @Entity
 */
@SpringBootApplication(scanBasePackages = { "com.apartment", "com.example.demo" })
@EnableJpaRepositories(basePackages = "com.apartment.repository")
@EntityScan(basePackages = "com.apartment.model")
public class ApartmentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApartmentManagementApplication.class, args);
	}

}
