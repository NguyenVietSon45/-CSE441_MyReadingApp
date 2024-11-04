package com.example.myreadingapp.Models;

public class Genre {
    private String id;
    private String name;

    // Constructor không tham số
    public Genre() {
    }

    // Constructor có tham số

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }


    // Getter và Setter


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // Hiển thị tên genre trong spinner
    }
}