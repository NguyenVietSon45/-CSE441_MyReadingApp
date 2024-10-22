package com.example.myreadingapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myreadingapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginName, loginPass;
    TextView txtForgotPassword;
    Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginName = findViewById(R.id.edt_loginName);
        loginPass = findViewById(R.id.edt_loginPass);
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_sign_up);
        txtForgotPassword = findViewById(R.id.txt_forgot_password);


        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()){
                    //if validateUsername()or validatePassword() false, nothing work
                } else {
                    checkUser();
                }
            }
        });

    }

    //kiem tra username empty
    public boolean validateUsername(){
        String val = loginName.getText().toString();
        if (val.isEmpty()){
            loginName.setError("Username cannot be empty.");
            return false;
        } else {
            loginName.setError(null);
            return true;
        }
    }

    //kiem tra password empty
    public boolean validatePassword(){
        String val = loginPass.getText().toString();
        if (val.isEmpty()){
            loginPass.setError("Password cannot be empty.");
            return false;
        } else {
            loginPass.setError(null);
            return true;
        }
    }

    //check user in database or not
    public void checkUser(){
        String username = loginName.getText().toString().trim();
        String password = loginPass.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        //compare username stored in dtb with username provided by user
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginName.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);

                    //compare password stored in dtb with password provided by user
                    if (passwordFromDB.equals(password)) {
                        loginName.setError(null);
                        // Mở MainActivity và điều hướng đến HomeFragment
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("FRAGMENT", "HOME"); // Thêm extra để xác định Fragment nào sẽ mở
                        startActivity(intent);
                        finish(); // Kết thúc LoginActivity
                    }
                    else{  //if password doesn't match
                        loginPass.setError("Invalid Credentials");
                        loginPass.requestFocus();
                    }
                } else {
                    loginName.setError("User does not exist");
                    loginName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}