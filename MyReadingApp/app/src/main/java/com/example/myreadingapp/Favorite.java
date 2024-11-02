package com.example.myreadingapp;

public class Favorite {
    private String manga_id;
    private String user_id;
    private String title;
    private String imageUrl;
    private Long created_at;

    // Constructor
    public Favorite(String manga_id, String user_id, String title, String imageUrl, Long created_at) {
        this.manga_id = manga_id;
        this.user_id = user_id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.created_at = created_at;
    }

    public Favorite() {

    }

    // Getters
    public String getManga_id() {
        return manga_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCreated_at() {
        return created_at;
    }

    //Setters
    public void setManga_id(String manga_id) {
        this.manga_id = manga_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}

