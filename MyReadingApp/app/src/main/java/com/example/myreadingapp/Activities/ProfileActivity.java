package com.example.myreadingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.myreadingapp.R;
import com.example.myreadingapp.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.myreadingapp.Models.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Switch switcher;
    boolean nightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ActivityProfileBinding binding;
    FirebaseDatabase database;
    DatabaseReference databaseRef;

    Button btnLogout, btnSetting;
    CircleImageView imgProfile;
    ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng binding để thiết lập nội dung view
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgProfile = findViewById(R.id.profile_image);
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            logout();
        });

        btnSetting = findViewById(R.id.btn_account_setting);
        btnSetting.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingProfileActivity.class);
                startActivity(intent);
            }
        }));

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //dark-light MODE
//      getSupportActionBar().hide(); //no need bcs parent="Theme.AppCompat.NoActionBar"
        switcher = findViewById(R.id.switch_MODE);

        //used sharedPreferences to save MODE  if exit the app and go back again
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false); //light MODE is default

        if (nightMODE) {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nightMODE) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });

//        if (savedInstanceState == null) {
//            replaceFragment(new HomeFragment()); // Chỉ thay thế khi activity được khởi tạo lần đầu
//        }

//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.action_home) {
//                replaceFragment(new HomeFragment());
//            } else if (item.getItemId() == R.id.action_archive) {
//                replaceFragment(new ArchieveFragment());
//            } else if (item.getItemId() == R.id.action_notice) {
//                replaceFragment(new NoticeFragment());
//            }
//            return true;
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Lấy avt_url người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", ""); // Lấy id người dùng

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
                Glide.with(ProfileActivity.this)
                        .load(curUser.getAvt_url())
                        .placeholder(R.drawable.none_avatar) // placeholder image
                        .error(R.drawable.none_avatar) // error image
                        .into(imgProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_profile, fragment);
        fragmentTransaction.commit();
    }

    private void logout() {
        // Xóa thông tin đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("id");
        editor.remove("password");
        editor.remove("username");
        editor.remove("avt_url");
        editor.apply();

        // In ra thông báo để kiểm tra
        Log.d("ProfileActivity", "Đăng xuất tài khoản thành công");

        // Chuyển hướng người dùng đến màn hình đăng nhập
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Kết thúc hoạt động của ProfileActivity
    }
}