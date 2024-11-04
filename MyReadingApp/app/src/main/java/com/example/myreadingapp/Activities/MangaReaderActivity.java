package com.example.myreadingapp.Activities;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myreadingapp.Models.Chapter;
import com.example.myreadingapp.Models.History;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;
import java.util.Map;

public class MangaReaderActivity extends AppCompatActivity {

    private TextView tvChapterTitle;
    private ImageButton btnPrevious, btnNext, btnBack;
    private Spinner spinnerChapter;
    private PDFView pdfView;
    private List<Chapter> chapterList = new ArrayList<>();
    private int currentChapterIndex = 0;
    private DatabaseReference historyRef, chapterRef;
    private String userId = "-OAafiSII85n1L0Czcn1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_reader);

        historyRef = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("history");
        chapterRef = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("chapters");

        tvChapterTitle = findViewById(R.id.tvChapterTitle);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        spinnerChapter = findViewById(R.id.spinnerChapter);
        pdfView = findViewById(R.id.pdfView);

        // Load chapter data from Firebase
        loadChaptersFromFirebase();

        // Set up button listeners
        btnPrevious.setOnClickListener(v -> changeChapter(currentChapterIndex - 1));
        btnNext.setOnClickListener(v -> changeChapter(currentChapterIndex + 1));
        btnBack.setOnClickListener(v -> finish()); // or getActivity().onBackPressed() for fragments


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
        String chapterId = getIntent().getStringExtra("chapter_id");

        if (mangaId != null) {
            Query query = chapterRef.orderByChild("manga_id").equalTo(mangaId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot chapterSnapshot) {
                    chapterList.clear(); // Clear the chapter list to avoid duplicates
                    int initialIndex = 0;
                    int index = 0;

                    for (DataSnapshot chapterData : chapterSnapshot.getChildren()) {
                        Chapter chapter = chapterData.getValue(Chapter.class);
                        if (chapter != null) {
                            chapterList.add(chapter);
                            // Find the initial index of the passed chapter_id
                            if (chapter.getId().equals(chapterId)) {
                                initialIndex = index;
                            }
                            index++;
                        }
                    }

                    if (!chapterList.isEmpty()) {
                        setupSpinner(initialIndex);
                    } else {
                        Toast.makeText(MangaReaderActivity.this, "No chapters found for this manga.", Toast.LENGTH_SHORT).show();
                    }
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


    private void setupSpinner(int initialIndex) {
        // Sort the chapterList by order to make sure it's in ascending order (top to bottom)
        Collections.sort(chapterList, Comparator.comparingInt(Chapter::getOrder));

        List<String> chapterNames = new ArrayList<>();
        for (Chapter chapter : chapterList) {
            chapterNames.add("Chapter " + chapter.getOrder());
        }

        // Find the index of the chapter with the provided chapter ID after sorting
        for (int i = 0; i < chapterList.size(); i++) {
            if (chapterList.get(i).getId().equals(getIntent().getStringExtra("chapter_id"))) {
                initialIndex = i;
                break;
            }
        }

        // Create a new adapter and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chapterNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChapter.setAdapter(adapter);

        // Set the initial selection to the specified chapter index
        spinnerChapter.setSelection(initialIndex);
        changeChapter(initialIndex);
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

        updateHistory(currentChapter.getId(), currentChapter.getManga_id());
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

    private void updateHistory(String chapterId, String mangaId) {
        historyRef.orderByChild("user_id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot matchingEntry = null;

                // Iterate over all entries to find one with the same user_id and manga_id
                for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                    History history = historySnapshot.getValue(History.class);
                    if (history != null && history.getManga_id().equals(mangaId)) {
                        matchingEntry = historySnapshot;
                        break; // Stop searching once a match is found
                    }
                }

                if (matchingEntry != null) {
                    // Update the existing entry with new chapter_id and last_date
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("chapter_id", chapterId);
                    updates.put("last_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

                    matchingEntry.getRef().updateChildren(updates)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("History", "Existing entry updated successfully");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("History", "Error updating the existing entry: " + e.getMessage());
                            });
                } else {
                    // Add new history entry if no matching entry is found
                    addNewHistoryEntry(chapterId, mangaId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("History", "Query cancelled", error.toException());
            }
        });
    }

    private void addNewHistoryEntry(String chapterId, String mangaId) {
        String historyId = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("history").push().getKey();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        History history = new History(historyId, userId, mangaId, chapterId, currentDate); // mangaId is NOT directly stored here.
        if (historyId != null) {
            FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("history").child(historyId).setValue(history).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "History updated", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to update history", Toast.LENGTH_SHORT).show();
            });
        }
    }
}




