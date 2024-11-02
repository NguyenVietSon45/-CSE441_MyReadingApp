package com.example.myreadingappli.Info;

public class Author {
    private String id;   // Đổi từ Long sang String
    private String name;

    // Constructor không tham số
    public Author() {
    }

    // Constructor có tham số
    public Author(String id, String name) {
        this.id = id;  // Sử dụng String
        this.name = name;
    }

    // Getter và Setter
    public String getId() {
        return id; // Trả về id kiểu String
    }

    public void setId(String id) {
        this.id = id; // Sử dụng String
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // Hiển thị tên tác giả trong spinner
    }
}