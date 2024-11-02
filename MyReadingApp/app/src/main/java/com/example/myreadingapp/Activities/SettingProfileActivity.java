package com.example.myreadingapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myreadingapplication.R;
import com.squareup.picasso.Picasso;

import com.example.myreadingapp.Models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingProfileActivity extends AppCompatActivity {

    private CircleImageView imgProfile;
    private ImageView btnBack;
    private TextView tv_name, tv_email;

    private String user_id;
    private User currentUser;
    private SharedPreferences sharedPreferences;

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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        usersDB.getUserById(user_id, new UserDB.UserCallback() {
//            @Override
//            public void onUserLoaded(User user) {
//                currentUser = user;
//                setupUI();
//            }
//        });
//    }

    //thiết lập giao diện người dùng dựa trên thông tin của người dùng hiện tại.
    private void setupUI() {
        if (currentUser == null) {
            return;
        }
        tv_name.setText(currentUser.getUsername());
        try{
            if (!currentUser.getAvt_url().equals(""))
                Picasso.get().load(currentUser.getAvt_url()).into(imgProfile);
            else
                imgProfile.setImageResource(R.drawable.none_avatar);
        }catch (Exception e){
            imgProfile.setImageResource(R.drawable.none_avatar);
        }
    }
}