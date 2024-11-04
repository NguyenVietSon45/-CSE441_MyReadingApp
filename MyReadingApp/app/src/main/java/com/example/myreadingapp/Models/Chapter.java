package com.example.myreadingapp.Models;

public class Chapter {
    private Long created_at;
    private String id;
    private String manga_id;
    private int order;
    private String pdf_url;

    public Chapter() {
    }

    public Chapter(Long created_at, String id, String manga_id, int order, String pdf_url) {
        this.created_at = created_at;
        this.id = id;
        this.manga_id = manga_id;
        this.order = order;
        this.pdf_url = pdf_url;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
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
}
