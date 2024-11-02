package com.example.myreadingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MangaViewHolder> {

    private Context mContext;
    private List<History> mListHistory;
    private DatabaseReference database;

    public HistoryAdapter(Context mContext) {
        this.mContext = mContext;
        database = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    }

    public void setData(List<History> list) {
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
        History history = mListHistory.get(position);
        if (history == null) {
            return;
        }

        Glide.with(mContext)
                .load(history.getImageUrl())
                .placeholder(R.drawable.placeholder_img) // placeholder image
                .error(R.drawable.error_img) // error image
                .into(holder.imgManga);
        holder.tvName.setText(history.getTitle());
        holder.tvChapter.setText("Chapter" + history.getOrder());
        holder.tvLastDate.setText("Last read: " + history.getLast_date());
        // Set click listener for the "Continue" button
        holder.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Direct to the latest chapter
                Intent intent = new Intent(mContext, MangaReaderActivity.class);
                intent.putExtra("CHAPTER_ID", history.getChapter_id());
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

            imgManga = itemView.findViewById(R.id.mangaThumbnail);
            tvName = itemView.findViewById(R.id.mangaTitle);
            tvChapter = itemView.findViewById(R.id.mangaChapter);
            tvLastDate = itemView.findViewById(R.id.lastDate);
            btnContinue = itemView.findViewById(R.id.btnContinue);
        }
    }
}
