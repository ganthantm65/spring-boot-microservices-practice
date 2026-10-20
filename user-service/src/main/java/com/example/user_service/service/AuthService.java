package com.example.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.user_service.DTO.LoginRequest;
import com.example.user_service.DTO.LoginResponse;
import com.example.user_service.config.JwtService;
import com.example.user_service.model.Admin;
import com.example.user_service.model.Users;
import com.example.user_service.repository.AdminRepo;
import com.example.user_service.repository.UserRepo;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public void registerUser(Users users) throws Exception {
        if (users.getEmail() == null || users.getPassword() == null || users.getUsername() == null) {
            throw new Exception("Fields are required");
        }

        Users user = userRepo.findByEmail(users.getEmail());

        if (user != null) {
            throw new Exception("User with this email already exists");
        }

        users.setPassword(passwordEncoder.encode(users.getPassword()));
        userRepo.save(users);
    }

    public void registerAdmin(Admin admin) throws Exception {
        if (admin.getEmail() == null || admin.getPassword() == null || admin.getAdminname() == null) {
            throw new Exception("Fields are required");
        }

        Admin existingAdmin = adminRepo.findByEmail(admin.getEmail());

        if (existingAdmin != null) {
            throw new Exception("Admin with this email already exists");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepo.save(admin);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) throws Exception {
        if (loginRequest == null ||
            loginRequest.getUserName() == null ||
            loginRequest.getUserName().isBlank() ||
            loginRequest.getPassword() == null ||
            loginRequest.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                "Username and password are required"
            );
        }
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Users users = userRepo.findByUsername(loginRequest.getUserName());

        if (users == null) {
            throw new Exception("User not found");
        }

        String token = jwtService.generateToken(userDetails);

        LoginResponse response = new LoginResponse();
        response.setUserId(users.getId());
        response.setUserName(users.getUsername());
        response.setToken(token);

        return response;
    }

    public LoginResponse loginAdmin(LoginRequest loginRequest) throws Exception {
        if (loginRequest == null ||
            loginRequest.getUserName() == null ||
            loginRequest.getUserName().isBlank() ||
            loginRequest.getPassword() == null ||
            loginRequest.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                "Username and password are required"
            );
        }
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid admin username or password");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Admin admin = adminRepo.findByAdminname(loginRequest.getUserName());

        if (admin == null) {
            throw new Exception("Admin not found");
        }

        String token = jwtService.generateToken(userDetails);

        LoginResponse response = new LoginResponse();
        response.setUserId(admin.getId());
        response.setUserName(admin.getAdminname());
        response.setToken(token);

        return response;
    }
}