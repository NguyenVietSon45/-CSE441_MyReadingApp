package com.example.myreadingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.myreadingapp.Info.Genre;
import com.example.myreadingapp.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenreAdapter extends ArrayAdapter<Genre> {

    public GenreAdapter(@NonNull Context context, int resource, @NonNull List<Genre> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_selected,parent,false);
        TextView tvSelected= convertView.findViewById(R.id.genre_selected);
        Genre genre=this.getItem(position);
        if (genre !=  null){
            tvSelected.setText(genre.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_catergory,parent,false);
        TextView tvCatergory= convertView.findViewById(R.id.genre_category);
        Genre genre=this.getItem(position);
        if (genre !=  null){
            tvCatergory.setText(genre.getName());
        }
        return convertView;
    }
}
