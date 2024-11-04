package com.example.myreadingapp.Models;

public class Chapter {
    private String title;
    private String id;
    private String manga_id;
    private int order;
    private String pdf_url;
    private long created_at;

    public Chapter() {
    }

    public Chapter(String title, String id, String manga_id, int order, String pdf_url, long created_at) {
        this.title = title;
        this.id = id;
        this.manga_id = manga_id;
        this.order = order;
        this.pdf_url = pdf_url;
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
