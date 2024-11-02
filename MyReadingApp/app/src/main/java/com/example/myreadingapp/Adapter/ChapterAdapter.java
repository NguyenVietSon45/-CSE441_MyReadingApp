package com.example.myreadingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myreadingapp.Models.Chapter;
import com.example.myreadingapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{

    private List<Chapter> mListChapter;

    public ChapterAdapter(List<Chapter> mListChapter) {
        this.mListChapter = mListChapter;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = mListChapter.get(position);
        if (chapter == null){
            return;
        }
        holder.tv_chapter.setText("Chapter " + chapter.getOrder());
        holder.tv_date.setText(chapter.getCreated_at().toString());
    }

    @Override
    public int getItemCount() {
        if (mListChapter != null){
            return mListChapter.size();
        }
        return 0;
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_chapter, tv_date;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_chapter = itemView.findViewById(R.id.chapter_tv);
            tv_date = itemView.findViewById(R.id.date_tv);
        }
    }
}
