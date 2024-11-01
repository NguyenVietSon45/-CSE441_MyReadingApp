package com.example.myreadingapp;

public class Manga {
    private String imageUrl;
    private String author;
    private String title;
    private String genre;
    private String summary;
    private Boolean favorite;
    private String recentChapter;

    public Manga(String imageUrl, String author, String title, String genre, String summary, Boolean favorite, String recentChapter) {
        this.imageUrl = imageUrl;
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.favorite = favorite;
        this.recentChapter = recentChapter;
    }

    public Manga() {

    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
