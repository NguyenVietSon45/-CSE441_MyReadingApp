package com.example.myreadingappli.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.myreadingappli.Info.Genre;
import com.example.myreadingappli.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenreAdapter extends ArrayAdapter<Genre> {


    public GenreAdapter(@NonNull Context context, int resource, @NonNull List<Genre> genres) {
        super(context, resource, genres);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Tối ưu hóa việc sử dụng convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.genre_selected, parent, false);
        }

        TextView tvSelected = convertView.findViewById(R.id.genre_selected);
        Genre genre = getItem(position);
        TextView textView = convertView.findViewById(R.id.genre_selected); // Đảm bảo ID này khớp với layout
        textView.setText(genre != null ? genre.getName() : "N/A");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Tối ưu hóa việc sử dụng convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.genre_catergory, parent, false);
        }

        // Lấy đối tượng Genre
        Genre genre = getItem(position);

        // Tìm kiếm TextView cho dropdown
        TextView tvCategory = convertView.findViewById(R.id.genre_category); // ID cho danh sách dropdown
        tvCategory.setText(genre != null ? genre.getName() : "N/A");

        return convertView;
    }
}
