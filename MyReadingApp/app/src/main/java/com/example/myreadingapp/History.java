package com.example.myreadingapp;

public class History {
    private String user_id;
    private String chapter_id;
    private String last_date;
    private String title;
    private String imageUrl;
    private int order;

    public History() {

    }

    public History(String user_id, String chapter_id, String last_date, String title, String imageUrl, int order) {
        this.user_id = user_id;
        this.chapter_id = chapter_id;
        this.last_date = last_date;
        this.title = title;
        this.imageUrl = imageUrl;
        this.order = order;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
