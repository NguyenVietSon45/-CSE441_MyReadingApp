package com.example.myreadingapp.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myreadingapp.Adapter.SortedMangaAdapter;
import com.example.myreadingapp.Models.Manga;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class  NoticeFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Manga> mangaList;
    private SortedMangaAdapter mangaAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        recyclerView = view.findViewById(R.id.rv_manga);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        mangaList = new ArrayList<>();
        mangaAdapter = new SortedMangaAdapter(mangaList);
        recyclerView.setAdapter(mangaAdapter);

        // Tải dữ liệu truyện từ Firebase
        loadMangaData();
        return view;
    }

    private void loadMangaData() {
        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("manga");
        mangaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mangaList.clear(); // Xóa danh sách trước đó
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String authorId = snapshot.child("authorId").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    long createdAt = snapshot.child("created_at").getValue(Long.class);

                    // Thêm manga vào danh sách
                    mangaList.add(new Manga(title, authorId, snapshot.getKey(), description, imageUrl, createdAt));
                }
                mangaAdapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Lỗi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}