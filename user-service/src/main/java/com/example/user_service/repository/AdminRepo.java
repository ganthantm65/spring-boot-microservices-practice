package com.example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.model.Admin;

public interface AdminRepo extends JpaRepository<Admin,Integer>{
    Admin findByAdminname(String admin_name);
    Admin findByEmail(String email);
}
