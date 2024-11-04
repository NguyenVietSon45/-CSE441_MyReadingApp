package com.example.myreadingapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myreadingapp.R;
import com.example.myreadingapp.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.example.myreadingapp.Models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingProfileActivity extends AppCompatActivity {

    private CircleImageView imgProfile;
    private ImageView btnBack;
    private TextView tv_username, tv_email;
    private EditText editName, editEmail, editPass;
    private Button btnUpdate;

    private String user_id;
    private User currentUser;
    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;
    private com.example.myreadingapp.databinding.ActivityProfileBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgProfile = findViewById(R.id.img_profile);
        // Nhận URL avatar từ Intent
        String avatarUrl = getIntent().getStringExtra("AVATAR_URL");
        loadAvatar(avatarUrl); // Tải avatar
        imgProfile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingProfileActivity.this, AvatarUpdateActivity.class);
                startActivity(intent);
            }
        }));

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // nhận dữ liệu
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        user_id = sharedPreferences.getString("id", "");

        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPass = findViewById(R.id.edit_password);
        btnUpdate = findViewById(R.id.btn_update_edit);

        btnUpdate.setOnClickListener(v -> updateUserInfo());
    }

    private void updateUserInfo() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPass.getText().toString().trim();

        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null); // Lấy ID người dùng
        Log.d("Setting Pro", "currentId:" + id);

        // Kiểm tra xem có ít nhất một thông tin nào được nhập không
        boolean isUpdated = false;

        databaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!name.isEmpty()) {
            databaseRef.child(id).child("username").setValue(name);
            editor.putString("username", name); // Lưu name vào SharedPreferences
            editor.apply(); // Lưu thay đổi
            isUpdated = true;
        }

        if (!email.isEmpty()) {
            databaseRef.child(id).child("email").setValue(email);
            editor.putString("email", email); // Lưu name vào SharedPreferences
            editor.apply(); // Lưu thay đổi
            isUpdated = true;
        }

        if (!password.isEmpty()) {
            databaseRef.child(id).child("password").setValue(password);
            isUpdated = true;
        }

        if (isUpdated) {
            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không có thông tin nào để cập nhật.", Toast.LENGTH_SHORT).show();
        }
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
    protected void onResume() {
        super.onResume();

        // Lấy avt_url người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", ""); // Lấy id người dùng
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");

        // Hiển thị tên người dùng trong TextView
        tv_username = findViewById(R.id.tv_name_user); // Ánh xạ TextView
        tv_username.setText(username); // Gán tên người dùng vào TextView
        tv_email = findViewById(R.id.tv_email);
        tv_email.setText(email);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("users");
        // Lấy dữ liệu từ Firebase Realtime Database
        databaseRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
                User curUser = snapshot.getValue(User.class);
//                if (curUser.getAvt_url().equals("")) {
//                    imgProfile.setImageResource(R.drawable.none_avatar);
//                } else {
//                    Picasso.get().load(curUser.getAvt_url()).into(imgProfile);
//                }
                Glide.with(SettingProfileActivity.this)
                        .load(curUser.getAvt_url())
                        .placeholder(R.drawable.none_avatar) // placeholder image
                        .error(R.drawable.none_avatar) // error image
                        .into(imgProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

//    //thiết lập giao diện người dùng dựa trên thông tin của người dùng hiện tại.
//    private void setupUI() {
//        if (currentUser == null) {
//            return;
//        }
//        tv_username.setText(currentUser.getUsername());
//        try{
//            if (!currentUser.getAvt_url().equals(""))
//                Picasso.get().load(currentUser.getAvt_url()).into(imgProfile);
//            else
//                imgProfile.setImageResource(R.drawable.none_avatar);
//        }catch (Exception e){
//            imgProfile.setImageResource(R.drawable.none_avatar);
//        }
//    }
}