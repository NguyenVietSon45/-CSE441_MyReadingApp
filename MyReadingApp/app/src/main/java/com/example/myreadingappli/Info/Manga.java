package com.example.myreadingappli.Info;

public class Manga {
    private String title;
    private String authorId; // Thay đổi từ Long sang String
    private String id;
    private String description;
    private String imageUrl;
    private long created_at;
//    private String user_id;

    // Constructor
    public Manga(String title, String authorId, String id, String description, String imageUrl,long created_at) {
        this.title = title;
        this.authorId = authorId; // Sử dụng String
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.created_at=created_at;
//        this.user_id=user_id;
    }


    // Constructor không tham số
    public Manga() {
    }

    // Getter và Setter

//    public String getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId; // Sử dụng String
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId; // Sử dụng String
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "title='" + title + '\'' +
                ", authorId='" + authorId + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}