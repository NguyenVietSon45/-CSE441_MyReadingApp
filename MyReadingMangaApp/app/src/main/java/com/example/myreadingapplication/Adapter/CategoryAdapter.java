package com.example.myreadingapplication.Adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myreadingapplication.Activities.HomeFragment;
import com.example.myreadingapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Models.Category;
import Models.Manga;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private Context mcontext;
    private List<Category> mListCategory;
    private List<Manga> mlistManga;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private MangaAdapter mangaAdapter;
    // truyền hết các list vào đây từ Home:
    // truyền từng list vào các adapter con

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
//        mangaAdapter.setData(category.getMangas());
        holder.rcvManga.setAdapter(mangaAdapter);

        getListMangaFromRealtimeDatabase();
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
            rcvManga = itemView.findViewById(R.id.rcv_manga);        }
    }

    private void getListMangaFromRealtimeDatabase(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("manga");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Manga manga = dataSnapshot.getValue(Manga.class);
                    mlistManga.add(manga);
                    Log.d("Tên:", manga.getTitle());
                }
                mangaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(requireContext(),"Get list manga failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
