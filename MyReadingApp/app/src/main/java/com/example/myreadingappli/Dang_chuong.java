package com.example.myreadingappli;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myreadingappli.Adapters.AuthorAdapter;
import com.example.myreadingappli.Adapters.MangaAdapter;
import com.example.myreadingappli.Info.Author;
import com.example.myreadingappli.Info.Chapter;
import com.example.myreadingappli.Info.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Dang_chuong extends AppCompatActivity {
    private EditText edit_chapters;
    private Spinner spn_title;
    private ImageView pdfUrl;
    private Uri pdfUri;
    private Button btn_upload;
    private String pdfFileName; // Biến lưu tên file PDF
    private String chapterTitle; // Biến lưu tiêu đề chương
    private String mangaId; // Biến lưu ID của manga
    private static final int PICK_PDF_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dang_chuong);
        initUI();
        loadManga();

        ImageView imageViewBack = findViewById(R.id.back_btn);
        imageViewBack.setOnClickListener(v -> {
            Intent intent = new Intent(Dang_chuong.this, tim_kiem_genre.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        pdfUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfChooser();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPDFAndChapter();
            }
        });
    }


    private void initUI() {
        spn_title = findViewById(R.id.spinner_title); // Đảm bảo ID này khớp với layout
        edit_chapters = findViewById(R.id.chapter); // Khai báo EditText cho chương
        pdfUrl = findViewById(R.id.pdf_upload); // Khai báo ImageView cho PDF
        btn_upload = findViewById(R.id.btn_upload); // Khai báo nút upload

    }
    private void loadManga() {
        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("manga");
        mangaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Manga> mangaList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Manga manga = snapshot.getValue(Manga.class);
                    if (manga != null) {
                        mangaList.add(manga);
                    }
                }

                if (mangaList.isEmpty()) {
                    Toast.makeText(Dang_chuong.this, "Không có truyện nào để hiển thị", Toast.LENGTH_SHORT).show();
                } else {
                    MangaAdapter adapter = new MangaAdapter(Dang_chuong.this, mangaList);
                    spn_title.setAdapter(adapter);

                    spn_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Manga selectedManga = (Manga) parent.getItemAtPosition(position);
                            mangaId = selectedManga.getId(); // Gán mangaId từ manga đã chọn
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Không làm gì cả
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dang_chuong.this, "Lỗi khi tải truyện", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openPdfChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Chọn file PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            pdfFileName = getFileName(pdfUri); // Lấy tên file
            Toast.makeText(this, "Đã chọn file PDF: " + pdfFileName, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPDFAndChapter() {
        if (pdfUri == null) {
            Toast.makeText(this, "Vui lòng chọn file PDF trước!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy giá trị từ EditText
        String chapterNumberString = edit_chapters.getText().toString().trim();
        if (chapterNumberString.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số chương!", Toast.LENGTH_SHORT).show();
            return;
        }

        int order;
        try {
            order = Integer.parseInt(chapterNumberString); // Chuyển đổi giá trị thành số nguyên
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số chương không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("pdfs/" + pdfFileName);

        storageRef.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String pdfUrlString = uri.toString();
                        // Sử dụng order đã nhập từ EditText
                        saveChapter(pdfUrlString, order); // Lưu chương mới với order từ EditText
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Dang_chuong.this, "Lỗi lấy URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Dang_chuong.this, "Lỗi tải lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveChapter(String pdfUrlString, int order) {
        Chapter newChapter = new Chapter(chapterTitle, null, mangaId, order, pdfUrlString, System.currentTimeMillis());
        DatabaseReference chaptersRef = FirebaseDatabase.getInstance().getReference("chapters");
        String chapterId = chaptersRef.push().getKey();
        newChapter.setId(chapterId); // Nếu bạn có phương thức setId

        chaptersRef.child(chapterId).setValue(newChapter).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Dang_chuong.this, "Đăng chương thành công!", Toast.LENGTH_SHORT).show();
                // Reset các trường nhập liệu nếu cần
            } else {
                Toast.makeText(Dang_chuong.this, "Đăng chương thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMaxOrder(Consumer<Integer> callback) {
        DatabaseReference chaptersRef = FirebaseDatabase.getInstance().getReference("chapters");

        // Lấy tất cả các chương của manga hiện tại
        chaptersRef.orderByChild("mangaId").equalTo(mangaId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maxOrder = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chapter chapter = snapshot.getValue(Chapter.class);
                    if (chapter != null) {
                        int chapterOrder = chapter.getOrder(); // Lấy thứ tự của chương
                        if (chapterOrder > maxOrder) {
                            maxOrder = chapterOrder; // Cập nhật maxOrder nếu lớn hơn
                        }
                    }
                }
                // Gọi callback với maxOrder
                callback.accept(maxOrder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dang_chuong.this, "Lỗi lấy dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                // Kiểm tra xem cursor có tồn tại và có dữ liệu không
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) { // Kiểm tra giá trị trả về
                        result = cursor.getString(columnIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            // Nếu không lấy được tên từ cursor, lấy từ đường dẫn
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}