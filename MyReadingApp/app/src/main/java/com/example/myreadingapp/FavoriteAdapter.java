package com.example.myreadingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MangaViewHolder>{

    private Context mContext;
    private List<Manga> mListManga;
    public FavoriteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Manga> list) {
        this.mListManga = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Manga manga = mListManga.get(position);
        if (manga == null) {
            return;
        }

        holder.imgManga.setImageResource(manga.getResourceId());
        holder.tvName.setText(manga.getName());
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
        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            imgManga = itemView.findViewById(R.id.mangaThumbnail);
            tvName = itemView.findViewById(R.id.mangaTitle);
        }
    }

}
