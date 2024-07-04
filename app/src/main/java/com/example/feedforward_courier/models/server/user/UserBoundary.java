package com.example.feedforward_courier.models.server.user;

import com.example.feedforward_courier.models.server.command.UserId;

public class UserBoundary {
    private UserId userId;//superApp name + email
    private RoleEnum role;
    private String username;
    private String avatar;


    public UserBoundary() {
    }

    public UserBoundary(String email, String username,RoleEnum role ,String avatar) {
        UserId userId = new UserId("2024b.gal.said", email);
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }


    public RoleEnum getRole() {
        return role;
    }


    public void setRole(RoleEnum role) {
        this.role = role;
    }


    public String getUserName() {
        return username;
    }


    public void setUserName(String userName) {
        this.username = userName;
    }


    public String getAvatar() {
        return avatar;
    }


    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public UserId getUserId() {
        return userId;
    }


    public void setUserId(UserId userId) {
        this.userId = userId;
    }
}
