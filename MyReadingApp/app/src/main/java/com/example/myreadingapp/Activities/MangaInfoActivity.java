package com.example.myreadingapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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

import com.bumptech.glide.Glide;
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
    //    private DatabaseReference mMangaRef = database.getReference("users");
    private DatabaseReference myChapterRef = database.getReference("chapters");

    private ImageView img_back, img_coverManga;
    private TextView tv_title, tv_author, tv_status, tv_description, tv_favorite;

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

        //lay du lieu tu id truyen
        String mangaID = getIntent().getStringExtra("MANGA_ID");

        if (mangaID != null) {
            Log.d("TargetActivity", "Received Manga ID: " + mangaID);
            // Use the mangaID as needed
        } else {
            Log.e("TargetActivity", "Manga ID not found in Intent");
        }
        getMangaById(mangaID);


        img_back = findViewById(R.id.btn_back);
        img_back.setOnClickListener(v -> {
            finish();
        });
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

    // tim truyen theo id
    private void getMangaById(String mangaID) {
        DatabaseReference mangaRef = database.getReference("manga");
        mangaRef.child(mangaID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Manga curManga = snapshot.getValue(Manga.class);
                if (curManga != null) {
                    Log.d("MangaInfoActivity", "Manga ID: " + curManga.getId());
                    Log.d("MangaInfoActivity", "Manga title: " + curManga.getTitle());

                    // Update UI components (thanh phan giao dien ng dung) voi details cua manga
                    tv_title = findViewById(R.id.tv_manga_title);
                    tv_title.setText(curManga.getTitle());

                    img_coverManga = findViewById(R.id.iv_manga_cover);
                    Glide.with(MangaInfoActivity.this).load(curManga.getImageUrl()).into(img_coverManga);

                    tv_author = findViewById(R.id.tv_author);
                    tv_author.setText(curManga.getAuthorId());

                    tv_description = findViewById(R.id.tv_description);
                    tv_description.setText(curManga.getDescription());

                } else {
                    Log.d("MangaInfoActivity", "Manga not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MangaInfoActivity", "Failed to fetch manga", error.toException());
            }
        });
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