package com.example.myreadingapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myreadingapplication.Adapter.CategoryAdapter;
import com.example.myreadingapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

import Models.Category;
import Models.Manga;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private ImageView imgProfile;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private List<Manga> listManga;
    private List<Category> listCategory;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ RecyclerView và adapter
        rcvCategory = view.findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(requireContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);


        listManga = new ArrayList<>();
        listCategory = new ArrayList<>();

//        categoryAdapter.setData(getListMangaFromRealtimeDatabase());
        rcvCategory.setAdapter(categoryAdapter);


        imgProfile = view.findViewById(R.id.profile_image);
        // Nhận URL avatar từ Intent
        if (getActivity() != null && getActivity().getIntent() != null) {
            String avatarUrl = getActivity().getIntent().getStringExtra("AVATAR_URL");
//            loadAvatar(avatarUrl); // Tải avatar
        }
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa tất cả Activity phía trên
                startActivity(intent);
                getActivity().finish();
                // Đóng MainActivity
            }
        });


        return view;
    }

    private void clickAddAllMangas(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("manga");

        List<Manga> listManga = new ArrayList<>();
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author1"));
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author2"));

        myRef.setValue(listManga, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(requireContext(), "Add all mangas success", Toast.LENGTH_SHORT).show();            }
        });
    }

//    private List<Category> getListMangaFromRealtimeDatabase(){
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = firebaseDatabase.getReference("manga");
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Manga manga = dataSnapshot.getValue(Manga.class);
//                    listManga.add(manga);
//                    Log.d("Tên:", manga.getTitle());
//                }
//                categoryAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(requireContext(),"Get list manga failed",Toast.LENGTH_SHORT).show();
//            }
//        });
//        return listCategory;
//    }
}