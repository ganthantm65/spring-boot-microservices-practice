package com.example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.model.Users;

public interface UserRepo extends JpaRepository<Users,Integer>{
    Users findByUsername(String user_name);
    
    Users findByEmail(String email);
}
