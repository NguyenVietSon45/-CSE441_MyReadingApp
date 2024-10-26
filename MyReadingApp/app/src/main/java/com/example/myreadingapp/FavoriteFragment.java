package com.example.myreadingapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Set your adapter here for the favorites list
        favoriteAdapter = new FavoriteAdapter(requireContext());

        favoriteAdapter.setData(getListManga());
        recyclerView.setAdapter(favoriteAdapter);

        return view;
    }

    private List<Manga> getListManga() {
        List<Manga> list = new ArrayList<>();
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House"));

        return list;
    }
}
