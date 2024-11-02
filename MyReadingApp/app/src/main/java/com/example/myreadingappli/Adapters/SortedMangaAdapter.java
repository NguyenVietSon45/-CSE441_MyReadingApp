package com.example.myreadingappli.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myreadingappli.Info.Manga;
import com.example.myreadingappli.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SortedMangaAdapter extends RecyclerView.Adapter<SortedMangaAdapter.MangaViewHolder> {
    private List<Manga> mangaList;

    public SortedMangaAdapter(List<Manga> mangaList) {
        this.mangaList = mangaList;
    }

    public void updateList(List<Manga> newList) {
        this.mangaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        holder.titleTextView.setText(manga.getTitle());
        Picasso.get().load(manga.getImageUrl()).into(holder.coverImageView);
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView coverImageView;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.manga_title); // ID của TextView trong item_manga
            coverImageView = itemView.findViewById(R.id.manga_cover); // ID của ImageView trong item_manga
        }
    }
}