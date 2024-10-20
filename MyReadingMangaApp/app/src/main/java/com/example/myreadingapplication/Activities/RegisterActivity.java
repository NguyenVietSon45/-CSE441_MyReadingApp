package com.example.myreadingapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myreadingapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class RegisterActivity extends AppCompatActivity {

    EditText registerName, registerMail, registerPass, registerConfirmPass;
    Button btnRegister;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_back);
        btnRegister = findViewById(R.id.btn_register);
        registerName = findViewById(R.id.edt_registerName);
        registerMail = findViewById(R.id.edt_registerMail);
        registerPass = findViewById(R.id.edt_registerPass);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = registerName.getText().toString().trim();
                String mail = registerMail.getText().toString().trim();
                String pass = registerPass.getText().toString().trim();

                User user = new User(name, mail, pass);
                reference.child(name).setValue(user);

                Toast.makeText(RegisterActivity.this, "You have sign up successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


}