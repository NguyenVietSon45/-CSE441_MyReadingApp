package com.example.myreadingapp.Activities;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myreadingapp.Adapter.ChapterAdapter;
import com.example.myreadingapp.Models.Chapter;
import com.example.myreadingapp.Models.Favorite;
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
    private TextView tv_title, tv_author, tv_status, tv_description;
    private ImageButton btnFavor;

    private RecyclerView rcvChapter;
    private ChapterAdapter mchapterAdapter;
    private List<Chapter> mListChapter;

    private boolean isFavorite = false;
    private String currentUserId;



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

        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("id", null); // Lấy ID người dùng
        Log.d("MangaInfo", "currentUser:" + currentUserId);

        //lay du lieu tu id truyen
        String currentMangaID = getIntent().getStringExtra("MANGA_ID");

        if (currentMangaID != null) {
            Log.d("TargetActivity", "Received Manga ID: " + currentMangaID);
            // Use the mangaID as needed
        } else {
            Log.e("TargetActivity", "Manga ID not found in Intent");
        }
        getMangaById(currentMangaID);

        initUi();

        btnFavor = findViewById(R.id.btn_favor);
        btnFavor.setOnClickListener(v -> {
            isFavorite = !isFavorite; // Đổi trạng thái yêu thích
            setFavoriteBtn(); // Cập nhật màu nút
            if (isFavorite) {
                addMangaToFavorites(currentMangaID); // Thêm manga vào danh sách yêu thích
            } else {
                removeMangaFromFavorites(currentMangaID); // Xóa manga khỏi danh sách yêu thích nếu cần
            }
        });
        img_back = findViewById(R.id.btn_back);
        img_back.setOnClickListener(v -> {
            finish();
        });
    }

    private void addMangaToFavorites(String currentMangaID) {
        String favoriteID = myChapterRef.push().getKey(); // Tạo một ID duy nhất cho favorite
        long currentTime = System.currentTimeMillis(); // Thời gian hiện tại

        Favorite favorite = new Favorite(favoriteID, currentMangaID, currentUserId, currentTime);

        DatabaseReference favoritesRef = database.getReference("favorites");
        favoritesRef.child(favoriteID).setValue(favorite)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MangaInfoActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MangaInfoActivity.this, "Không thể thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                });
    }

    private void removeMangaFromFavorites(String currentMangaID) {
//        DatabaseReference favoritesRef = database.getReference("favorites");
//        // Tìm kiếm và xóa yêu thích dựa vào user_id và manga_id
//        favoritesRef.orderByChild("user_id").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean found = false; // Flag to check if a favorite was found
//                for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
//                    Favorite favorite = favoriteSnapshot.getValue(Favorite.class);
//                    if (favorite != null && favorite.getManga_id().equals(currentMangaID)) {
//                        favoritesRef.child(favoriteSnapshot.getKey()).removeValue()
//                                .addOnSuccessListener(aVoid -> {
//                                    Toast.makeText(MangaInfoActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> {
//                                    Toast.makeText(MangaInfoActivity.this, "Không thể xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
//                                });
//                        found = true; // Set the flag if a favorite is found
//                    }
//                }
//                if (!found) {
//                    Toast.makeText(MangaInfoActivity.this, "Manga not found in favorites", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MangaInfoActivity.this, "Failed to remove favorite", Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    private void initUi(){
        rcvChapter = findViewById(R.id.rcv_chapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvChapter.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvChapter.addItemDecoration(dividerItemDecoration);

        mListChapter = new ArrayList<>();
        mchapterAdapter = new ChapterAdapter(this, mListChapter);
        rcvChapter.setAdapter(mchapterAdapter);
    }

    private void setFavoriteBtn() {
        if (isFavorite) {
            btnFavor.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        } else {
            btnFavor.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }
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

                    getListChapter(curManga.getId());
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

    //lấy list chapter theo id cua manga
    private void getListChapter(String mangaId) {
        DatabaseReference myChapterRef = database.getReference("chapters");
        myChapterRef.orderByChild("manga_id").equalTo(mangaId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mListChapter.clear();  // Xóa danh sách cũ trước khi thêm mới
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chapter chapter = dataSnapshot.getValue(Chapter.class);
                    if (chapter != null) {
                        mListChapter.add(chapter);
                        Log.d("MangaInfoActivity","Chapter: " + chapter.getId());
                        Log.d("MangaInfo", "Oder: " + chapter.getOrder());
                    }
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