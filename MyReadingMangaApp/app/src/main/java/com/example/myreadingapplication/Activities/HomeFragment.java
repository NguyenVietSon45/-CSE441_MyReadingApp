package com.example.myreadingapplication.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myreadingapplication.Adapter.CategoryAdapter;
import com.example.myreadingapplication.R;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

        rcvCategory = view.findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(requireContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);
        return view;
    }

    private List<Category> getListCategory(){
        List<Category> listCategory = new ArrayList<>();

        List<Manga> listManga = new ArrayList<>();
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author1"));
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author2"));
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author3"));
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author4"));

        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author1"));
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author2"));
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author3"));
        listManga.add(new Manga(R.drawable.poster, "Moriaty: The Patriorty","Author4"));

        listCategory.add(new Category("Favorite", listManga));
        listCategory.add(new Category("Trending", listManga));
        listCategory.add(new Category("Favorite", listManga));
        listCategory.add(new Category("Trending", listManga));

        return listCategory;
    }
}