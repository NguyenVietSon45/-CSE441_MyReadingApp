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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import Models.User;

public class RegisterActivity extends AppCompatActivity {

    EditText registerName, registerMail, registerPass, registerConfirmPass;
    Button btnRegister;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView btnBack;

    //kiểm tra định dạng email
    private boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

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
        registerConfirmPass = findViewById(R.id.edt_registerConfirmPass);

        btnBack.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("users");

            String username = registerName.getText().toString().trim();
            String mail = registerMail.getText().toString().trim();
            String pass = registerPass.getText().toString().trim();
            String confirmPass = registerConfirmPass.getText().toString().trim();

            //kiểm tra mật khẩu và xác nhận mật khẩu trùng khớp
            if (!pass.equals(confirmPass)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            //kiểm tra định dạng email
            if (!isEmailValid(mail)) {
                Toast.makeText(RegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem email đã tồn tại trong database hay chưa
            //orderByChild("email").equalTo(mail):truy vấn Firebase để tìm kiếm người dùng có email trùng khớp.
            //addListenerForSingleValueEvent: nhận dữ liệu một lần và sau đó ngừng lắng nghe.
            //dataSnapshot.exists(): Nếu tồn tại một bản ghi trùng vs email đã nhập, hiển thị thông báo rằng email đã tồn tại
            reference.orderByChild("email").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tự động sinh ID duy nhất và lấy thời gian hiện tại để gán vào id và created_at của User
                        DatabaseReference newUserRef = reference.push();
                        String id = newUserRef.getKey();
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        User user = new User(id, username, mail, pass, "https://i2.wp.com/vdostavka.ru/wp-content/uploads/2019/05/no-avatar.png?w=512&ssl=1", currentTime);
                        newUserRef.setValue(user);

                        Toast.makeText(RegisterActivity.this, "You have signed up successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }

                //Khi có lỗi trong quá trình truy vấn, Firebase sẽ gọi hàm onCancelled() và truyền vào một đối tượng DatabaseError.
                //DatabaseError để lấy thông tin về lỗi và hiển thị cho người dùng
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(RegisterActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

//            // Trong RegisterActivity, sau khi người dùng đăng ký thành công:
//            Intent intent = new Intent(RegisterActivity.this, Dang_truyen.class);
//            intent.putExtra("USER_ID", id); // Gửi user_id
//            startActivity(intent);
        });
    }
}