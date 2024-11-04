package com.example.myreadingapp.Models;

public class Manga {
    private String id;
    private String title;
    private String imageUrl;
    private String authorId;
    private String description;
    private long created_at;

    public Manga(String id, String title, String imageUrl, String authorId, String description, long created_at) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.authorId = authorId;
        this.description = description;
        this.created_at = created_at;
    }

    public Manga() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
}
