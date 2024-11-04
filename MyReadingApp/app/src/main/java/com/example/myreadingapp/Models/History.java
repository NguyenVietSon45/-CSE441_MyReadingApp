package com.example.myreadingapp.Models;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class History {
    private String history_id;
    private String user_id;
    private String manga_id;
    private String chapter_id;
    private String last_date;

    public History() {

    }

    public History(String history_id, String user_id, String manga_id, String chapter_id, String last_date) {
        this.history_id = history_id;
        this.user_id = user_id;
        this.manga_id = manga_id;
        this.chapter_id = chapter_id;
        this.last_date = last_date;
    }

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getManga_id() {
        return manga_id;
    }

    public void setManga_id(String manga_id) {
        this.manga_id = manga_id;
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

    // Method to retrieve 'order' dynamically from the 'Chapter' table
    public void getOrder(DatabaseReference chapterRef, OrderCallback callback) {
        chapterRef.child(chapter_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chapter chapter = snapshot.getValue(Chapter.class);
                if (chapter != null) {
                    callback.onOrderReceived(chapter.getOrder());
                } else {
                    callback.onOrderReceived(-1); // Handle case where chapter is not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onOrderReceived(-1); // Handle failure
            }
        });
    }

    // Callback interface to handle asynchronous order retrieval
    public interface OrderCallback {
        void onOrderReceived(int order);
    }
}
