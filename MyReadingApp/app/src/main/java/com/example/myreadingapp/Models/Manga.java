package com.example.myreadingapp.Models;

public class Manga {
    private String imageUrl;
    private String authorId;
    private String title;
    private Long created_at;
    private String description;
    private String id;

    public Manga(String imageUrl, String authorId, String title, Long created_at, String description, String id) {
        this.imageUrl = imageUrl;
        this.authorId = authorId;
        this.title = title;
        this.created_at = created_at;
        this.description = description;
        this.id = id;
    }

    public Manga() {

    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
