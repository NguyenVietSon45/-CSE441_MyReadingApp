package com.example.myreadingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MangaViewHolder> {

    private Context mContext;
    private List<Manga> mListManga;

    public HistoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Manga> list) {
        this.mListManga = list;
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
        Manga manga = mListManga.get(position);
        if (manga == null) {
            return;
        }

        Glide.with(mContext)
                .load(manga.getImageUrl())
                .placeholder(R.drawable.placeholder_img) // placeholder image
                .error(R.drawable.error_img) // error image
                .into(holder.imgManga);
        holder.tvName.setText(manga.getTitle());
        holder.tvChapter.setText(manga.getChapter());
    }

    @Override
    public int getItemCount() {
        if (mListManga != null) {
            return mListManga.size();
        }
        return 0;
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgManga;
        private TextView tvName;
        private TextView tvChapter;
        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            imgManga = itemView.findViewById(R.id.mangaThumbnail);
            tvName = itemView.findViewById(R.id.mangaTitle);
            tvChapter = itemView.findViewById(R.id.mangaChapter);
        }
    }
}
