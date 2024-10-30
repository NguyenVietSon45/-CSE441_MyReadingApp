package com.example.myreadingapp;

public class Manga {
    private int resourceId;
    private String name;
    private String chapter;
    public Manga(int resourceId, String name, String chapter) {
        this.resourceId = resourceId;
        this.name = name;
        this.chapter = chapter;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
