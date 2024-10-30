package com.example.myreadingapp;

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

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Set your adapter here for the history list
        historyAdapter = new HistoryAdapter(requireContext());

        historyAdapter.setData(getListManga());
        recyclerView.setAdapter(historyAdapter);

        return view;
    }

    private List<Manga> getListManga() {
        List<Manga> list = new ArrayList<>();
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "1/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "2/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "3/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "4/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "5/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "6/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "7/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "8/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "9/80"));
        list.add(new Manga(R.drawable.sample_manga, "Shadow House", "10/80"));

        return list;
    }
}
