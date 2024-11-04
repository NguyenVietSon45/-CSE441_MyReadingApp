package com.example.myreadingapp.Models;

public class MangaGenreDB {
    private String id;          // ID của MangaGenre
    private String id_truyen;   // ID của truyện
    private String id_genre;    // ID của genre

    // Constructor không tham số
    public MangaGenreDB() {
    }

    // Constructor có tham số
    public MangaGenreDB(String id, String id_truyen, String id_genre) {
        this.id = id;
        this.id_truyen = id_truyen;
        this.id_genre = id_genre;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_truyen() {
        return id_truyen;
    }

    public void setId_truyen(String id_truyen) {
        this.id_truyen = id_truyen;
    }

    public String getId_genre() {
        return id_genre;
    }

    public void setId_genre(String id_genre) {
        this.id_genre = id_genre;
    }
}