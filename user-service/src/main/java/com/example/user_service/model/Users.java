package com.example.user_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity 
@Table(name = "users")
public class Users {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    private String username;

    private String password;

    @Email 
    private String email;

    public void setUserName(String user_name){
        this.username=user_name;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setEmail(String email){
        this.email=email;
    } 

    public Integer getId(){
        return this.user_id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }
}
