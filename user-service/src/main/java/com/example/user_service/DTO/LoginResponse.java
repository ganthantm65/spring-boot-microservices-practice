package com.example.user_service.DTO;

public class LoginResponse {

    private Integer userId;
    private String userName;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(Integer userId, String userName, String token) {
        this.userId = userId;
        this.userName = userName;
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}