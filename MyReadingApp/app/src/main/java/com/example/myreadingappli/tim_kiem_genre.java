package com.example.myreadingappli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myreadingappli.Adapters.GenreAdapter;
import com.example.myreadingappli.Adapters.SortedMangaAdapter;
import com.example.myreadingappli.Info.Genre;
import com.example.myreadingappli.Info.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tim_kiem_genre extends AppCompatActivity {
    private Spinner spnGenre;
    private LinearLayout searchLayout;
    private ImageView sortImageView;
    private Button btnSearch;
    private SearchView searchView;
    private GenreAdapter genreAdapter;
    private SortedMangaAdapter mangaAdapter;
    private RecyclerView recyclerView;
    private List<Genre> genreList;
    private List<Manga> mangaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tim_kiem_genre);
        UIcreated();
        ImageView imageViewBack = findViewById(R.id.back_btn);
        imageViewBack.setOnClickListener(v -> {
            Intent intent = new Intent(tim_kiem_genre.this, Chapter_notify.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Thiết lập RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        mangaList = new ArrayList<>();
        mangaAdapter = new SortedMangaAdapter(mangaList);
        recyclerView.setAdapter(mangaAdapter);

        // Thiết lập sự kiện nhấn cho ImageView
        sortImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchLayout();
            }
        });

        // Tải dữ liệu thể loại từ Firebase
        loadGenresFromDatabase();

        // Thiết lập listener cho Spinner
        spnGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Genre selectedGenre = (Genre) parent.getItemAtPosition(position);
                if (selectedGenre != null) {
//                    searchMangaByGenre(selectedGenre.getId()); // Tìm kiếm tự động khi chọn thể loại
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Thiết lập sự kiện nhấn cho nút tìm kiếm
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Có thể thêm logic tìm kiếm khác nếu cần
                Genre selectedGenre = (Genre) spnGenre.getSelectedItem();
                if (selectedGenre != null) {
                    searchMangaByGenre(selectedGenre.getId());
                } else {
                    Toast.makeText(tim_kiem_genre.this, "Vui lòng chọn thể loại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMangaByTitle(query); // Gọi phương thức tìm kiếm dựa trên tiêu đề
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    private void UIcreated(){
        spnGenre = findViewById(R.id.spn_genre_1);
        sortImageView = findViewById(R.id.sort);
        searchLayout = findViewById(R.id.search_layout);
        btnSearch = findViewById(R.id.btn_search);
        recyclerView = findViewById(R.id.rv_sortedManga);
        searchView = findViewById(R.id.search_view); // Khởi tạo SearchView
    }

    private void loadGenresFromDatabase() {
        DatabaseReference genresRef = FirebaseDatabase.getInstance().getReference("genre");
        genresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                genreList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Genre genre = snapshot.getValue(Genre.class);
                    if (genre != null) {
                        genreList.add(genre);
                    }
                }
                updateSpinner(genreList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(tim_kiem_genre.this, "Lỗi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSpinner(List<Genre> genres) {
        genreAdapter = new GenreAdapter(this, R.layout.genre_selected, genres);
        spnGenre.setAdapter(genreAdapter);
    }

    private void toggleSearchLayout() {
        if (searchLayout.getVisibility() == View.GONE) {
            searchLayout.setVisibility(View.VISIBLE);
        } else {
            searchLayout.setVisibility(View.GONE);
        }
    }

    private void searchMangaByGenre(String genreId) {
        DatabaseReference mangaGenreRef = FirebaseDatabase.getInstance().getReference("manga_genre");
        mangaGenreRef.orderByChild("id_genre").equalTo(genreId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mangaList.clear(); // Xóa danh sách manga trước đó
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String mangaId = snapshot.child("id_truyen").getValue(String.class);
                    if (mangaId != null) {
                        fetchMangaDetails(mangaId);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(tim_kiem_genre.this, "Lỗi truy vấn dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMangaDetails(String mangaId) {
        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("manga").child(mangaId);
        mangaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String authorId = dataSnapshot.child("authorId").getValue(String.class);
                String description = dataSnapshot.child("description").getValue(String.class);
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                long createdAt = dataSnapshot.child("created_at").getValue(Long.class);

                mangaList.add(new Manga(title, authorId, mangaId, description, imageUrl, createdAt));
                mangaAdapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(tim_kiem_genre.this, "Lỗi tải manga: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void searchMangaByTitle(String query) {
        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("manga");
        mangaRef.orderByChild("title").startAt(query).endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mangaList.clear(); // Xóa danh sách manga trước đó
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String title = snapshot.child("title").getValue(String.class);
                            String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                            // Chỉ thêm title và imageUrl vào danh sách
                            mangaList.add(new Manga(title, null, null, null, imageUrl, 0)); // Giả sử bạn không cần các thuộc tính khác
                        }
                        mangaAdapter.notifyDataSetChanged(); // Cập nhật adapter
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(tim_kiem_genre.this, "Lỗi tìm kiếm manga: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}