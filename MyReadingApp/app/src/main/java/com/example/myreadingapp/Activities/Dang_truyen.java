package com.example.myreadingapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myreadingapp.Adapter.AuthorAdapter;
import com.example.myreadingapp.Adapter.GenreAdapter;
import com.example.myreadingapp.Models.MangaGenreDB;
import com.example.myreadingapp.Models.Author;
import com.example.myreadingapp.Models.Genre;
import com.example.myreadingapp.Models.Manga;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Dang_truyen extends AppCompatActivity {
    private Spinner spinnerGenre, spinnerAuthor;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imgeUrl;
    private EditText editTextTitle,editTextAuthor, editTextSummary;
    private Button buttonSubmit;
    private String genreIDSelected;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dang_truyen);
        UI();

//        Intent intent = getIntent();
//        user_id = intent.getStringExtra("USER_ID");

        ImageView imageViewBack = findViewById(R.id.back_btn);
        imageViewBack.setOnClickListener(v -> {
            Intent intent = new Intent(Dang_truyen.this, Dang_chuong.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        loadGenres();
        loadAuthors();

        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Genre selectedGenre = (Genre) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không có gì được chọn
            }
        });

        imgeUrl.setOnClickListener(v -> openFileChooser());

        buttonSubmit.setOnClickListener(v -> uploadFile());
    }

    private void UI() {
        editTextTitle = findViewById(R.id.edit_title);
        imgeUrl = findViewById(R.id.img_url);
        spinnerAuthor = findViewById(R.id.spinner_author);
        editTextSummary = findViewById(R.id.edit_sum);
        spinnerGenre = findViewById(R.id.spn_genre);
        buttonSubmit = findViewById(R.id.btn_upload);
    }

    private void loadGenres() {
        DatabaseReference genresRef = FirebaseDatabase.getInstance().getReference("genre");
        genresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Genre> genreList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Genre genre = snapshot.getValue(Genre.class);
                    if (genre != null) {
                        genreList.add(genre); // Thêm đối tượng Genre vào danh sách
                    }
                }

                if (genreList.isEmpty()) {
                    Toast.makeText(Dang_truyen.this, "Không có thể loại nào để hiển thị", Toast.LENGTH_SHORT).show();
                } else {
                    GenreAdapter adapter = new GenreAdapter(Dang_truyen.this, R.layout.genre_selected, genreList);
                    spinnerGenre.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dang_truyen.this, "Lỗi khi tải thể loại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAuthors() {
        DatabaseReference authorsRef = FirebaseDatabase.getInstance().getReference("authors");
        authorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Author> authorList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Author author = snapshot.getValue(Author.class);
                    if (author != null) {
                        authorList.add(author);
                    }
                }

                if (authorList.isEmpty()) {
                    Toast.makeText(Dang_truyen.this, "Không có tác giả nào để hiển thị", Toast.LENGTH_SHORT).show();
                } else {
                    AuthorAdapter adapter = new AuthorAdapter(Dang_truyen.this, authorList);
                    spinnerAuthor.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dang_truyen.this, "Lỗi khi tải tác giả", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgeUrl.setImageURI(imageUri);
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");
            String fileName = System.currentTimeMillis() + ".jpg";
            StorageReference fileReference = storageReference.child(fileName);

            UploadTask uploadTask = fileReference.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    saveComicInfo(downloadUrl);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(Dang_truyen.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Không có hình ảnh nào được chọn", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveComicInfo(String downloadUrl) {
        String title = editTextTitle.getText().toString().trim();
        Author selectedAuthor = (Author) spinnerAuthor.getSelectedItem();
        String authorId = selectedAuthor != null ? selectedAuthor.getId() : null; // Lấy ID của tác giả


        Genre selectedGenre = (Genre) spinnerGenre.getSelectedItem();
        String genreId = selectedGenre != null ? selectedGenre.getId() : null; // Lấy ID của genre

        String description = editTextSummary.getText().toString().trim();

        if (title.isEmpty() || authorId == null || genreId == null || description.isEmpty()) {
            Toast.makeText(Dang_truyen.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();

            return;
        }

        DatabaseReference comicRef = FirebaseDatabase.getInstance().getReference("manga");
        String mangaId = comicRef.push().getKey();
        long created_at= System.currentTimeMillis();
        Manga manga = new Manga(title, authorId, mangaId, description, downloadUrl,created_at);
        comicRef.child(mangaId).setValue(manga).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveMangaGenre(mangaId, genreId);
                Toast.makeText(Dang_truyen.this, "Đăng truyện thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Dang_truyen.this, "Đăng truyện thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMangaGenre(String mangaId, String genreId) {
        MangaGenreDB mangaGenre = new MangaGenreDB(null, mangaId, genreId);
        DatabaseReference mangaGenreRef = FirebaseDatabase.getInstance().getReference("manga_genre");
        String mangaGenreId = mangaGenreRef.push().getKey();
        if (mangaGenreId != null) {
            mangaGenre.setId(mangaGenreId);
            mangaGenreRef.child(mangaGenreId).setValue(mangaGenre)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lưu thành công
                        } else {
                            // Lưu thất bại
                        }
                    });
        }
    }
}