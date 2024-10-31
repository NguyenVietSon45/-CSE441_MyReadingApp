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

    EditText loginEmail, loginPass;
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

        loginEmail = findViewById(R.id.edt_loginEmail);
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
                if (!validateEmail() | !validatePassword()){
                    //if validateEmail()or validatePassword() false, nothing work
                } else {
                    checkUser();
                }
            }
        });

    }

    //kiem tra email empty
    public boolean validateEmail(){
        String val = loginEmail.getText().toString();
        if (val.isEmpty()){
            loginEmail.setError("Email cannot be empty.");
            return false;
        } else {
            loginEmail.setError(null);
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
        String email = loginEmail.getText().toString().trim();
        String password = loginPass.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        //compare email stored in dtb with email provided by user
        Query checkUserDatabase = reference.orderByChild("email").equalTo(email);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginEmail.setError(null);
                    String passwordFromDB = null; // Khai báo biến ở đây
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        passwordFromDB = userSnapshot.child("password").getValue(String.class);
                    }
                    //snapshot.getKey(): trả về khóa (ID) của nút người dùng trong cơ sở dữ liệu Firebase.
                    //Objects.requireNonNull() đảm bảo rằng snapshot.getKey() không trả về null trước khi sử dụng nó.
                    //Sau đó, chúng ta sử dụng child(snapshot.getKey()) để truy cập vào nút người dùng dựa trên ID.


                    // print the values for debugging
                    System.out.println("passwordFromDB: " + passwordFromDB);
                    System.out.println("password: " + password);


                    //compare password stored in dtb with password provided by user
                    if (passwordFromDB != null && passwordFromDB.equals(password)) {
                        loginEmail.setError(null);
                        loginPass.setError(null);

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
                    loginEmail.setError("User does not exist");
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}