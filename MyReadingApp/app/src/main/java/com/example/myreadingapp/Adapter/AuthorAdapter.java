package com.example.myreadingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.myreadingapp.Models.Author;
import com.example.myreadingapp.R;

import java.util.List;

public class AuthorAdapter extends ArrayAdapter<Author> {

    public AuthorAdapter(Context context, List<Author> authors) {
        super(context, R.layout.author_selected, authors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra xem view có tồn tại hay không, nếu không thì tạo mới
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.author_selected, parent, false);
        }

        // Lấy đối tượng Author tại vị trí đã cho
        Author author = getItem(position);

        // Thiết lập tên tác giả vào TextView
        TextView textView = convertView.findViewById(R.id.author_selected);
        textView.setText(author != null ? author.getName() : "N/A");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Tương tự như getView nhưng cho dropdown
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.author_catergory, parent, false);
        }

        Author author = getItem(position);
        TextView textView = convertView.findViewById(R.id.author_category);
        textView.setText(author != null ? author.getName() : "N/A");

        return convertView;
    }
}