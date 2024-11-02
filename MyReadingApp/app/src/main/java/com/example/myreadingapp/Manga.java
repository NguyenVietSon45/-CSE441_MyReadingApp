package com.example.myreadingapp;

public class Manga {
    private String imageUrl;
    private String authorId;
    private String title;
    private String genre;
    private String summary;
    private Boolean favorite;
    private String recentChapter;
    private Long created_at;
    private String description;
    private String id;

    public Manga(String imageUrl, String authorId, String title, String genre, String summary, Boolean favorite, String recentChapter, Long created_at, String description, String id) {
        this.imageUrl = imageUrl;
        this.authorId = authorId;
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.favorite = favorite;
        this.recentChapter = recentChapter;
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

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getRecentChapter() {
        return recentChapter;
    }

    public void setRecentChapter(String recentChapter) {
        this.recentChapter = recentChapter;
    }
}
