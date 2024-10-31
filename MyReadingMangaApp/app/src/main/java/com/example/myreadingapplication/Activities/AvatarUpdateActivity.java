package com.example.myreadingapplication.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myreadingapplication.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarUpdateActivity extends AppCompatActivity {

    ImageView imgProfile, btnBack;
    Button btnUpload, btnSave, btnDelete;

    private Uri uriImage; // Biến để lưu trữ URI của ảnh
    private DatabaseReference databaseReference;
    private StorageReference storageProfilePicsRef;

//    private String myUri = "";
//    private StorageTask uploadTask;
//    private StorageReference storageProfilePicsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avatar_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

//        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getColor(R.color.light_grey)));

        imgProfile = findViewById(R.id.img_avt);
        // Nhận URL avatar từ Intent
        String avatarUrl = getIntent().getStringExtra("AVATAR_URL");
        loadAvatar(avatarUrl); // Tải avatar

        btnUpload = findViewById(R.id.btn_upload_avt);
        btnSave = findViewById(R.id.btn_save_avt);
        btnDelete = findViewById(R.id.btn_delete_avt);

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("profile_pics");        //Lấy đối tượng StorageReference để truy cập và thao tác dữ liệu trong Firebase Storage, cụ thể là thư mục "Profile Pic".


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AvatarUpdateActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(v -> {
            if (uriImage != null) {
                uploadProfileImage(); // Gọi phương thức upload hình ảnh
            } else {
                Toast.makeText(this, "Vui lòng chọn ảnh trước!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void loadAvatar(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            avatarUrl = String.valueOf(R.drawable.none_avatar);
        }

        Glide.with(this)
                .load(avatarUrl)
                .error(R.drawable.none_avatar)
                .into(imgProfile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uriImage = data.getData();
            imgProfile.setImageURI(uriImage);
        }
    }

    private void uploadProfileImage() {
        StorageReference filePath = storageProfilePicsRef.child(System.currentTimeMillis() + ".jpg");
        filePath.putFile(uriImage).addOnSuccessListener(taskSnapshot -> {
            filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                databaseReference.push().child("user").child("avt_url").setValue(downloadUrl).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AvatarUpdateActivity.this, "Cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AvatarUpdateActivity.this, "Cập nhật ảnh thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(AvatarUpdateActivity.this, "Upload ảnh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}