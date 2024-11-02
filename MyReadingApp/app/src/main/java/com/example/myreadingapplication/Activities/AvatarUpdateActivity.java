package com.example.myreadingapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarUpdateActivity extends AppCompatActivity {

    CircleImageView imgProfile;
    ImageView btnBack;
    Button btnUpload, btnSave, btnDelete;
    String userId;

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


        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", null); // Lấy ID người dùng

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
        if (userId != null) {
            System.out.println("User ID: " + userId);
        } else {
            System.out.println("Failed to get user ID");
        }

        StorageReference filePath = storageProfilePicsRef.child(userId + "avt");
        UploadTask uploadTask = filePath.putFile(uriImage);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                databaseReference.child(userId).child("avt_url")
                        .setValue(uri.toString())
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        });
            });
        });

    }
}