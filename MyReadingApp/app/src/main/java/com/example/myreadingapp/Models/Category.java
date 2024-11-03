package com.example.myreadingapp.Models;

import java.util.List;

public class Category {
    private String nameCategory;
    private List<Manga> mangas;

    public Category(String nameCategory, List<Manga> mangas) {
        this.nameCategory = nameCategory;
        this.mangas = mangas;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Manga> getMangas() {
        return mangas;
    }

    public void setMangas(List<Manga> mangas) {
        this.mangas = mangas;
    }
}

