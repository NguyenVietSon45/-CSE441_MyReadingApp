package com.example.myreadingapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MangaReaderActivity extends AppCompatActivity {

    private TextView tvChapterTitle;
    private ImageButton btnPrevious, btnNext;
    private Spinner spinnerChapter;
    private PDFView pdfView;

    private List<Chapter> chapterList = new ArrayList<>();
    private int currentChapterIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_reader);

        tvChapterTitle = findViewById(R.id.tvChapterTitle);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        spinnerChapter = findViewById(R.id.spinnerChapter);
        pdfView = findViewById(R.id.pdfView);

        // Load chapter data from Firebase
        loadChaptersFromFirebase();

        // Set up button listeners
        btnPrevious.setOnClickListener(v -> changeChapter(currentChapterIndex - 1));
        btnNext.setOnClickListener(v -> changeChapter(currentChapterIndex + 1));

        spinnerChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != currentChapterIndex) {
                    changeChapter(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadChaptersFromFirebase() {
        String mangaId = getIntent().getStringExtra("manga_id");
        if (mangaId != null) {
            DatabaseReference chapterRef = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("chapters");
            Query query = chapterRef.orderByChild("manga_id").equalTo(mangaId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot chapterSnapshot) {
                    chapterList.clear();
                    for (DataSnapshot chapterData : chapterSnapshot.getChildren()) {
                        Chapter chapter = chapterData.getValue(Chapter.class);
                        if (chapter != null) {
                            chapterList.add(chapter);
                        }
                    }
                    setupSpinner();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MangaReaderActivity.this, "Failed to load chapter data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MangaReaderActivity.this, "Manga ID not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSpinner() {
        List<String> chapterNames = new ArrayList<>();
        for (Chapter chapter : chapterList) {
            chapterNames.add("Chapter " + chapter.getOrder());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chapterNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChapter.setAdapter(adapter);
        if (currentChapterIndex == 0 && !chapterList.isEmpty()) {
            changeChapter(0);
        }
    }

    private void changeChapter(int newIndex) {
        if (newIndex < 0 || newIndex >= chapterList.size()) {
            Toast.makeText(this, "No more chapters", Toast.LENGTH_SHORT).show();
            return;
        }
        currentChapterIndex = newIndex;
        Chapter currentChapter = chapterList.get(currentChapterIndex);
        tvChapterTitle.setText("Chapter " + currentChapter.getOrder());
        loadPdf(currentChapter.getPdf_url());
    }

    private void loadPdf(String pdfUrl) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            pdfView.fromStream(inputStream)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .load();
        });
    }
}



