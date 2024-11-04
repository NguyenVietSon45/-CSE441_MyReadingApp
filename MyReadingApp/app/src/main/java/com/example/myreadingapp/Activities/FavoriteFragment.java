package com.example.myreadingapp.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myreadingapp.Adapter.FavoriteAdapter;
import com.example.myreadingapp.Models.Favorite;
import com.example.myreadingapp.Models.Manga;
import com.example.myreadingapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private ArrayList<Favorite> list;
    private DatabaseReference database;

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

        // Initialize the database reference
        database = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        // Start listening for real-time updates
        listenForFavoriteMangaUpdates("-OAafiSII85n1L0Czcn1"); // Replace "userIdPlaceholder" with the actual user ID logic

        return view;
    }

    private void listenForFavoriteMangaUpdates(String userId) {
        DatabaseReference favoritesRef = database.child("favorites");

        favoritesRef.orderByChild("user_id").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                handleFavoriteMangaChange(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                handleFavoriteMangaChange(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String mangaId = snapshot.child("manga_id").getValue(String.class);
                if (mangaId != null) {
                    // Remove the manga from the list
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getManga_id().equals(mangaId)) {
                            list.remove(i);
                            favoriteAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle if necessary (usually not needed for this use case)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error listening for real-time updates: " + error.getMessage());
            }
        });
    }

    private void handleFavoriteMangaChange(DataSnapshot favoriteSnapshot) {
        String mangaId = favoriteSnapshot.child("manga_id").getValue(String.class);
        Long createdAt = favoriteSnapshot.child("created_at").getValue(Long.class);
        String userId = favoriteSnapshot.child("user_id").getValue(String.class);

        if (mangaId != null && userId != null) {
            // Fetch manga details
            DatabaseReference mangaRef = database.child("manga").child(mangaId);
            mangaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot mangaSnapshot) {
                    Manga manga = mangaSnapshot.getValue(Manga.class);
                    if (manga != null && manga.getTitle() != null) {
                        // Check if the manga already exists in the list and update or add it
                        boolean exists = false;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getManga_id().equals(mangaId)) {
                                list.set(i, new Favorite(
                                        mangaId,
                                        userId,
                                        manga.getTitle(),
                                        manga.getImageUrl(),
                                        createdAt
                                ));
                                favoriteAdapter.notifyItemChanged(i);
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            list.add(new Favorite(
                                    mangaId,
                                    userId,
                                    manga.getTitle(),
                                    manga.getImageUrl(),
                                    createdAt
                            ));
                            favoriteAdapter.notifyItemInserted(list.size() - 1);
                        }
                    } else {
                        System.err.println("Invalid manga data found in Firebase for manga ID: " + mangaId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.err.println("Error retrieving manga data from Firebase: " + error.getMessage());
                }
            });
        } else {
            System.err.println("Invalid favorite data found in Firebase");
        }
    }
}


