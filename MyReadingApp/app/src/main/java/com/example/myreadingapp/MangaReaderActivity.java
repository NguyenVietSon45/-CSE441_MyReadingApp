package com.example.myreadingapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MangaReaderActivity extends AppCompatActivity {

    private TextView tvChapter;
    private int currentChapter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_reader);

        tvChapter = findViewById(R.id.tvChapter);
        tvChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChapterDialog();
            }
        });
    }

    // Show a dialog to pick chapters
    private void showChapterDialog() {
        // Sample list of chapters
        final String[] chapters = {"Chapter 1", "Chapter 2", "Chapter 3"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Chapter")
                .setItems(chapters, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the chapter based on user selection
                        currentChapter = which + 1;
                        tvChapter.setText(chapters[which]);
                        loadChapter(currentChapter);
                    }
                });
        builder.create().show();
    }

    // Load the selected chapter
    private void loadChapter(int chapter) {
        // Logic to load the manga page for the selected chapter
        // Example: change ImageView source or load data from server
    }
}
