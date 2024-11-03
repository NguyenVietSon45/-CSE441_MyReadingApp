package com.example.myreadingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myreadingapp.R;

import java.util.List;

import com.example.myreadingapp.Models.Manga;
import com.squareup.picasso.Picasso;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder>{


    private List<Manga> mMangas;
    private CategoryAdapter.OnCategoryListener onCategoryListener;

    public void setData (List<Manga> list, CategoryAdapter.OnCategoryListener onCategoryListener){
        this.mMangas = list;
        this.onCategoryListener = onCategoryListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Manga manga = mMangas.get(position);
        if (manga == null){
            return;
        }
        if (manga.getImageUrl() != "") {
            Glide.with(holder.imgPoster.getContext())
                    .load(manga.getImageUrl())
                    .into(holder.imgPoster);
        }
        holder.tvTitle.setText(manga.getTitle());
        holder.tvAuthor.setText(manga.getAuthorId());
        holder.cardManga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryListener.onCategoryClick(manga);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mMangas != null){
            return mMangas.size();
        }
        return 0;
    }


    public  class MangaViewHolder extends RecyclerView.ViewHolder{

        private CardView cardManga;
        private TextView tvTitle;
        private TextView tvAuthor;
        private ImageView imgPoster;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            cardManga = itemView.findViewById(R.id.card_manga_poster);
            tvTitle = itemView.findViewById(R.id.tv_manga_title);
            tvAuthor = itemView.findViewById(R.id.tv_manga_author);
            imgPoster = itemView.findViewById(R.id.img_manga_poster);
        }
    }

}
