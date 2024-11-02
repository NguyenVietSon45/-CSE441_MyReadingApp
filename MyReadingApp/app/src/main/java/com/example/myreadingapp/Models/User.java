package com.example.myreadingapp.Models;

public class User {
    String id;
    String username;
    String email;
    String password;
    String avt_url;
    String created_at;

    public User() {
    }

    public User(String id, String username, String email, String password, String avt_url, String created_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avt_url = avt_url;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvt_url() {
        return avt_url;
    }

    public void setAvt_url(String avt_url) {
        this.avt_url = avt_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
