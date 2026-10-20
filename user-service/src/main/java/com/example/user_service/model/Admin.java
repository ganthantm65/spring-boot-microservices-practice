package com.example.user_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity 
@Table (name = "admin")
public class Admin {
    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Integer admin_id;
    private String adminname;

    private String password;

    @Email 
    private String email;

    public void setAdminName(String user_name){
        this.adminname=user_name;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setEmail(String email){
        this.email=email;
    } 

    public Integer getId(){
        return this.admin_id;
    }

    public String getAdminname(){
        return this.adminname;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }
}
