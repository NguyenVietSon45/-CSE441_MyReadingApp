package com.example.myreadingapplication.DB;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.User;

public class UserDB {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersRef;

    public UserDB() {
        mDatabase = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app");
        mUsersRef = mDatabase.getReference("users"); // Khởi tạo mUsersRef
    }

    public interface UserCallback {
        void onUserLoaded(User user);
    }

    public interface UpdateUserCallback {
        void onSuccess();
    }

    public interface AllUsersCallback {
        void onAllUsersLoaded(List<User> users);
    }
    //lay danh sach Users
    public void getAllUsers(final AllUsersCallback callback){
        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.add(user);
                }
                callback.onAllUsersLoaded(users);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // lấy user theo id
    public void getUserById(String userId, final UserCallback callback) {
        mUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                callback.onUserLoaded(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // them user
    public void addUser(User user) {
        String username = mUsersRef.push().getKey();
        user.setUsername(username);
        mUsersRef.child(username).setValue(user);

        mUsersRef = FirebaseDatabase.getInstance().getReference("users");

        String Id = mUsersRef.push().getKey(); // Tạo ID độc nhất
        User data = new User(/* tham số */);
        mUsersRef.child(Id).setValue(data);
    }

    //tim username bang email
    public void getUsernameByEmail(final String userName, final UserDB.UsernameCallback callback) {
        mUsersRef.orderByChild("Id").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        callback.onUsernameLoaded(userId);
                        return;
                    }
                }
                callback.onUsernameLoaded(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onUsernameLoaded(null);
            }
        });
    }

    public interface UsernameCallback {
        void onUsernameLoaded(String username);
    }
}
