package com.example.myreadingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;


import com.example.myreadingapp.Models.Manga;
import com.example.myreadingapp.R;

import java.util.List;

public class MangaViewAdapter extends ArrayAdapter<Manga> {

    public MangaViewAdapter(@NonNull Context context, @NonNull List<Manga> mangas) {
        super(context, R.layout.manga_selected, mangas);
    }

    // ViewHolder để tối ưu hóa hiệu suất

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tối ưu hóa việc sử dụng convertView
        // Kiểm tra xem view có tồn tại hay không, nếu không thì tạo mới
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.manga_selected, parent, false);
        }

        // Lấy đối tượng Author tại vị trí đã cho
        Manga manga= getItem(position);

        // Thiết lập tên tác giả vào TextView
        TextView textView = convertView.findViewById(R.id.manga_selected);
        textView.setText(manga != null ? manga.getTitle() : "N/A");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        // Tương tự như getView nhưng cho dropdown
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.manga_catergory, parent, false);
        }

        Manga manga = getItem(position);
        TextView textView = convertView.findViewById(R.id.manga_category);
        textView.setText(manga != null ? manga.getTitle() : "N/A");

        return convertView;
    }
}