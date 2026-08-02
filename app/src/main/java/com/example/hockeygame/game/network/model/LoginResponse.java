package com.example.hockeygame.game.network.model;

public class LoginResponse {

    private Long id;
    private String username;
    private String email;
    private String role;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}