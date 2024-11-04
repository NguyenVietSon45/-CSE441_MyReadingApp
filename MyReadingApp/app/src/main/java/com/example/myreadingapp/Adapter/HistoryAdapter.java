package com.example.myreadingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myreadingapp.Models.Chapter;
import com.example.myreadingapp.Models.History;
import com.example.myreadingapp.Models.Manga;
import com.example.myreadingapp.Activities.MangaReaderActivity;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MangaViewHolder> {

    private Context mContext;
    private List<History> mListHistory;
    private DatabaseReference database;

    public HistoryAdapter(Context mContext) {
        this.mContext = mContext;
        database = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    }

    public void setData(List<History> list) {
        if (list != null) {
            // Sort the list by last_date in descending order (newest first)
            Collections.sort(list, (h1, h2) -> {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date date1 = dateFormat.parse(h1.getLast_date());
                    Date date2 = dateFormat.parse(h2.getLast_date());
                    // Compare dates to ensure newer ones come first
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    Log.e("DateSortError", "Error parsing date", e);
                    return 0;
                }
            });
        }
        this.mListHistory = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HistoryAdapter.MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MangaViewHolder holder, int position) {
        History history = mListHistory.get(holder.getAdapterPosition());
        if (history == null) {
            return;
        }

        DatabaseReference chapterRef = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("chapters");
        DatabaseReference mangaRef = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("manga");

        Log.d("HistoryAdapter", "Attempting to load chapter data for ID: " + history.getChapter_id());

        // Retrieve the chapter using chapter_id from history
        chapterRef.child(history.getChapter_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chapterSnapshot) {
                Log.d("HistoryAdapter", "Chapter data loaded for ID: " + history.getChapter_id());
                Chapter chapter = chapterSnapshot.getValue(Chapter.class);
                if (chapter != null) {
                    // Now use the manga_id from the chapter to get manga details
                    mangaRef.child(chapter.getManga_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot mangaSnapshot) {
                            Manga manga = mangaSnapshot.getValue(Manga.class);
                            if (manga != null) {
                                // Display the image and title in the ViewHolder
                                holder.tvName.setText(manga.getTitle());
                                Glide.with(mContext)
                                        .load(manga.getImageUrl())
                                        .placeholder(R.drawable.placeholder_img) // placeholder image
                                        .error(R.drawable.error_img) // error image
                                        .into(holder.imgManga);
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

                    // Set the chapter order in the ViewHolder
                    holder.tvChapter.setText("Chapter " + chapter.getOrder());
                } else {
                    holder.tvChapter.setText("Chapter unknown");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HistoryAdapter", "Database error: " + error.getMessage());
                holder.tvChapter.setText("Chapter load failed");
            }
        });

        // Display the last read date
        try {
            SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = fullFormat.parse(history.getLast_date());

            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateOnly = dateOnlyFormat.format(date);

            holder.tvLastDate.setText(dateOnly);
        } catch (ParseException e) {
            Log.e("DateParseError", "Error parsing date", e);
            holder.tvLastDate.setText(history.getLast_date());
        }

        // Set click listener for the "Continue" button
        holder.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Direct to the latest chapter
                Intent intent = new Intent(mContext, MangaReaderActivity.class);
                intent.putExtra("chapter_id", history.getChapter_id());
                intent.putExtra("manga_id", history.getManga_id());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListHistory != null) {
            return mListHistory.size();
        }
        return 0;
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgManga;
        private TextView tvName;
        private TextView tvChapter;
        private TextView tvLastDate;
        private Button btnContinue;
        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            imgManga = itemView.findViewById(R.id.historyImage);
            tvName = itemView.findViewById(R.id.historyTitle);
            tvChapter = itemView.findViewById(R.id.mangaChapter);
            tvLastDate = itemView.findViewById(R.id.lastDate);
            btnContinue = itemView.findViewById(R.id.btnContinue);
        }
    }
}
