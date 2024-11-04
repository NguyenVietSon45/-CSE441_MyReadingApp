package com.example.myreadingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myreadingapp.Activities.MangaInfoActivity;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import com.example.myreadingapp.Models.Category;
import com.example.myreadingapp.Models.Manga;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private Context mcontext;
    private List<Category> mListCategory;
    private List<Manga> mlistManga;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private MangaAdapter mangaAdapter;

    public interface OnCategoryListener{
        void onCategoryClick(Manga manga);
    }

    public CategoryAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void setData(List<Category> list){
        this.mListCategory = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category == null){
            return;
        }
        holder.tvNameCategory.setText(category.getNameCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext, RecyclerView.HORIZONTAL, false);
        holder.rcvManga.setLayoutManager(linearLayoutManager);

        mangaAdapter = new MangaAdapter();

        mangaAdapter = new MangaAdapter();
        mangaAdapter.setData(category.getMangas(), new OnCategoryListener(){
            @Override
            public void onCategoryClick(Manga manga) {
                onDetailManga(manga);
            }
        });


        holder.rcvManga.setAdapter(mangaAdapter);
    }

    private void onDetailManga(Manga manga) {
        Intent intent = new Intent(mcontext, MangaInfoActivity.class);
        intent.putExtra("MANGA_ID", manga.getId());
        mcontext.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        if (mListCategory != null){
            return mListCategory.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameCategory;
        private RecyclerView rcvManga;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCategory = itemView.findViewById(R.id.tv_name_category);
            rcvManga = itemView.findViewById(R.id.rcv_manga);
        }
    }

}
