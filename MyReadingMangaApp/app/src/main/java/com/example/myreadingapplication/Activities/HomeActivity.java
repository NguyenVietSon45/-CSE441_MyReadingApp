package com.example.myreadingapplication.Activities;

import static android.app.ProgressDialog.show;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.myreadingapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.myreadingapplication.Activities.HomeFragment;
import com.example.myreadingapplication.Activities.ArchieveFragment;
import com.example.myreadingapplication.Activities.NoticeFragment;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//DÒNG DƯỚI NÀY NÈ, NÃYC HỊ XOÁ NHẦM :V
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_home) {
                    Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.action_archive) {
                    Toast.makeText(HomeActivity.this, "Archive", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.action_notice) {
                    Toast.makeText(HomeActivity.this, "Notice", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });


    }
}