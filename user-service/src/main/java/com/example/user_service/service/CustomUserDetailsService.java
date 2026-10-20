package com.example.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.user_service.model.Admin;
import com.example.user_service.model.Users;
import com.example.user_service.repository.AdminRepo;
import com.example.user_service.repository.UserRepo;

@Service 
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired 
    private UserRepo userRepo;
    @Autowired 
    private AdminRepo adminRepo;

    @Override 
    public UserDetails loadUserByUsername(String user_name) throws UsernameNotFoundException{
        Users user=userRepo.findByUsername(user_name);

        if(user!=null){
            return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
        }

        Admin admin=adminRepo.findByAdminname(user_name);

        if(admin!=null){
            return User.builder()
                .username(admin.getAdminname())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
        }

        throw new UsernameNotFoundException(user_name);
    }
}
