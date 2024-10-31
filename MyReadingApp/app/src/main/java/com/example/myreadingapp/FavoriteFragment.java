package com.example.myreadingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private ArrayList<Manga> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        list = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(requireContext());
        favoriteAdapter.setData(list);

        recyclerView.setAdapter(favoriteAdapter);

        DatabaseReference database = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("readingapp_db/comic");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Manga manga = dataSnapshot.getValue(Manga.class);
                    if (manga != null && manga.getFavorite() != null && manga.getFavorite() && manga.getTitle() != null) {
                        list.add(manga);
                    } else {
                        System.err.println("Invalid manga data found in Firebase");
                    }
                }
                favoriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error retrieving data from Firebase: " + error.getMessage());
            }
        });
        return view;
    }
}
