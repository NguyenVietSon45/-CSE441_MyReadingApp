package com.example.myreadingapp.Activities;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myreadingapp.Adapter.HistoryAdapter;
import com.example.myreadingapp.Models.History;
import com.example.myreadingapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<History> list;
    private DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        list = new ArrayList<>();
        historyAdapter = new HistoryAdapter(requireContext());
        historyAdapter.setData(list);

        recyclerView.setAdapter(historyAdapter);

        // Initialize the database reference
        database = FirebaseDatabase.getInstance("https://myreadingapp-39e7b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("id", ""); // Replace "" with a default value if not found
        listenForHistoryUpdates(user_id);

        return view;
    }

    private void listenForHistoryUpdates(String userId) {
        DatabaseReference historyRef = database.child("history");
        historyRef.orderByChild("user_id").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                handleHistoryChange(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                handleHistoryChange(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Not needed for this use case
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error retrieving history data: " + error.getMessage());
            }
        });
    }

    private void handleHistoryChange(DataSnapshot historySnapshot) {
        History history = historySnapshot.getValue(History.class);
        if (history != null) {
            boolean exists = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getChapter_id().equals(history.getChapter_id())) {
                    list.set(i, history);
                    historyAdapter.notifyItemChanged(i);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                list.add(history);
                historyAdapter.notifyItemInserted(list.size() - 1);
            }
        }
    }

}
