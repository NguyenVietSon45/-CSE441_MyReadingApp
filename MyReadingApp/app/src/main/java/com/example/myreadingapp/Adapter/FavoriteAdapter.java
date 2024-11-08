package com.example.myreadingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.myreadingapp.Activities.MangaInfoActivity;
import com.example.myreadingapp.Models.Chapter;
import com.example.myreadingapp.Models.Favorite;
import com.example.myreadingapp.Activities.MangaReaderActivity;
import com.example.myreadingapp.Models.Manga;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MangaViewHolder> {

    private Context mContext;
    private List<Favorite> mListFavorite;
    private DatabaseReference database;

    public FavoriteAdapter(Context mContext) {
        this.mContext = mContext;
        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    }

    public void setData(List<Favorite> list) {
        this.mListFavorite = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Favorite favorite = mListFavorite.get(holder.getAdapterPosition());
        if (favorite == null) {
            return;
        }

        DatabaseReference mangaRef = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("manga");

        mangaRef.child(favorite.getManga_id()).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot mangaSnapshot) {
              Manga manga = mangaSnapshot.getValue(Manga.class);
              if (manga != null) {
                  Glide.with(mContext)
                          .load(manga.getImageUrl())
                          .placeholder(R.drawable.placeholder_img) // placeholder image
                          .error(R.drawable.error_img) // error image
                          .into(holder.imgManga);
                  holder.tvName.setText(manga.getTitle());
              } else {
                  holder.tvName.setText("Title not found");
                  holder.imgManga.setImageResource(R.drawable.error_img);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
              holder.tvName.setText("Failed to load title");
              holder.imgManga.setImageResource(R.drawable.error_img);
          }
      });
                // Set click listener for the read button
                holder.btnRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MangaInfoActivity.class);
                        // Pass the manga ID or other necessary data
                        intent.putExtra("MANGA_ID", favorite.getManga_id());
                        mContext.startActivity(intent);
                    }
                });

        // Set click listener for the remove button
        holder.btnRemoveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) return;

                Favorite currentManga = mListFavorite.get(currentPosition);

                // Delete the corresponding node in the "favorites" node in Firebase
                database.child("favorites")
                        .orderByChild("manga_id")
                        .equalTo(currentManga.getManga_id())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                                    // Ensure the user ID matches before deleting (optional)
                                    if (favoriteSnapshot.child("user_id").getValue(String.class).equals(currentManga.getUser_id())) {
                                        favoriteSnapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.err.println("Error deleting favorite: " + error.getMessage());
                            }
                        });

                // Remove item from the list and notify adapter
                mListFavorite.remove(currentPosition);
                notifyItemRemoved(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListFavorite != null) {
            return mListFavorite.size();
        }
        return 0;
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgManga;
        private TextView tvName;
        private Button btnRemoveFavorite;
        private Button btnRead;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            imgManga = itemView.findViewById(R.id.favoriteImage);
            tvName = itemView.findViewById(R.id.favoriteTitle);
            btnRemoveFavorite = itemView.findViewById(R.id.btnContinue);
            btnRead = itemView.findViewById(R.id.btnRead);
        }
    }
}

