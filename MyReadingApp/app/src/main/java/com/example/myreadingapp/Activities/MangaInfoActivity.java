package com.example.myreadingapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myreadingapp.Adapter.ChapterAdapter;
import com.example.myreadingapp.Models.Chapter;
import com.example.myreadingapp.Models.Manga;
import com.example.myreadingapp.Models.User;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MangaInfoActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mMangaRef = database.getReference("users");
    private DatabaseReference myChapterRef = database.getReference("chapters");


    private RecyclerView rcvChapter;
    private ChapterAdapter mchapterAdapter;
    private List<Chapter> mListChapter;

    private Manga currentManga;
    private String user_id = "";
    String manga_id = "";
    private String id_favorite = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manga_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();

        manga_id = "-OAcCoaTjRmvRreR5KHk";
//        getMangaById(manga_id);

//        getListChapter();
    }

    private void initUi(){
        rcvChapter = findViewById(R.id.rcv_chapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvChapter.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvChapter.addItemDecoration(dividerItemDecoration);

        mListChapter = new ArrayList<>();
        mchapterAdapter = new ChapterAdapter(mListChapter);

        rcvChapter.setAdapter(mchapterAdapter);
    }

    // tìm truyện theo id
    private void getMangaById(String mangaID) {
        Log.d("DMMMMM2", mangaID);

        mMangaRef.child("-OAafiSII85n1L0Czcn1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User curManga = snapshot.getValue(User.class);
                Log.d("DMMMMM3", "title");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        Log.d("DMMMMM4", "title");
    }

    private void getListChapter(){
        myChapterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chapter chapter = dataSnapshot.getValue(Chapter.class);
                    mListChapter.add(chapter);
                    Log.d("Chapter: ", chapter.getId());
                }
                mchapterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MangaInfoActivity.this, "Get list chapter failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}