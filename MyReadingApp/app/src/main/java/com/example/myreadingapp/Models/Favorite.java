package com.example.myreadingapp.Models;

public class Favorite {
    private String id;
    private String manga_id;
    private String user_id;
    private Long created_at;

    // Constructor
    public Favorite(String id, String manga_id, String user_id, Long created_at) {
        this.id = id;
        this.manga_id = manga_id;
        this.user_id = user_id;
        this.created_at = created_at;
    }

    public Favorite() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManga_id() {
        return manga_id;
    }

    public void setManga_id(String manga_id) {
        this.manga_id = manga_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}

