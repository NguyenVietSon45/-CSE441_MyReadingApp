package com.example.myreadingapp.Activities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myreadingapp.Adapter.CategoryAdapter;
import com.example.myreadingapp.Models.User;
import com.example.myreadingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.myreadingapp.Models.Category;
import com.example.myreadingapp.Models.Manga;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private Context mBase;
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private ImageView imgProfile;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    private List<Manga> listManga, listFavManga, listHisManga;
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
//        Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ RecyclerView và adapter
        rcvCategory = view.findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(requireContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("id", "");



        listManga = new ArrayList<Manga>();
        listHisManga = new ArrayList<Manga>();
        getHistoryList(user_id);
        listCategory = new ArrayList<Category>();

        fetchMangaData();

        listCategory.add(new Category("Trending", listManga));
        listCategory.add(new Category("Favorite", listManga));
        listCategory.add(new Category("History", listHisManga));
        listCategory.add(new Category("Category", listManga));
        categoryAdapter.setData(listCategory);

//        categoryAdapter.setData(getListMangaFromRealtimeDatabase());
        rcvCategory.setAdapter(categoryAdapter);

        imgProfile = view.findViewById(R.id.profile_image);
        // Nhận URL avatar từ Intent
        if (getActivity() != null && getActivity().getIntent() != null) {
            String avatarUrl = getActivity().getIntent().getStringExtra("AVATAR_URL");
//            loadAvatar(avatarUrl); // Tải avatar
        }
        imgProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa tất cả Activity phía trên
            startActivity(intent);
            getActivity().finish();
            // Đóng MainActivity
        });

        // Lấy avt_url người dùng từ SharedPreferences
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String id = sharedPreferences.getString("id", ""); // Lấy id người dùng

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("users");
        // Lấy dữ liệu từ Firebase Realtime Database
        myRef.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
                User curUser = snapshot.getValue(User.class);
                Glide.with(requireContext())
                        .load(curUser.getAvt_url())
                        .placeholder(R.drawable.none_avatar) // placeholder image
                        .error(R.drawable.none_avatar) // error image
                        .into(imgProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        return mBase.getSharedPreferences(name, mode);
    }

    //lấy dữ liệu manga từ Firebase và cập nhật danh sách manga
    private void fetchMangaData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("manga"); //tham chiếu đến nút manga

        //Lắng nghe sự thay đổi dữ liệu
        myRef.addValueEventListener(new ValueEventListener() {
            //xử lý dữ liệu khi có thay đổi
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listManga.clear(); //Xóa tất cả các phần tử trong danh sách listManga để chuẩn bị cập nhật với dữ liệu mới
                //vòng lặp qua tất cả các con của snapshot (mỗi con là 1 manga)
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Manga manga = dataSnapshot.getValue(Manga.class);
                    listManga.add(manga);
                }
                categoryAdapter.notifyDataSetChanged(); //Thông báo cho adapter rằng dữ liệu đã thay đổi
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getHistoryList(String userId) {
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("history");
        DatabaseReference mangaRef = FirebaseDatabase.getInstance().getReference("mangas");

        historyRef.orderByChild("user_id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Manga> tempList = new ArrayList<>();
                int totalItems = (int) dataSnapshot.getChildrenCount(); // Total number of manga entries to process

                if (totalItems == 0) {
                    //Neu khong co item nao, update list va exit
                    listHisManga.clear();
                    listHisManga.addAll(tempList);
                    categoryAdapter.notifyDataSetChanged();
                    return;
                }

                final int[] processedItems = {0}; // Counter to keep track of completed callbacks

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String mangaId = snapshot.child("manga_id").getValue(String.class);

                    if (mangaId != null) {
                        mangaRef.child(mangaId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot mangaSnapshot) {
                                String title = mangaSnapshot.child("title").getValue(String.class);
                                String imageUrl = mangaSnapshot.child("imageUrl").getValue(String.class);
                                String authorId = mangaSnapshot.child("author_id").getValue(String.class);
                                String description = mangaSnapshot.child("description").getValue(String.class);
                                Long createdAt = mangaSnapshot.child("created_at").getValue(Long.class);
                                long createdAtValue = (createdAt != null) ? createdAt : 0L;

                                Manga manga = new Manga(mangaId, title, imageUrl, authorId, description, createdAtValue);
                                tempList.add(manga);

                                processedItems[0]++; // Increment the counter
                                if (processedItems[0] == totalItems) {
                                    // All items have been processed, update the list
                                    listHisManga.clear();
                                    listHisManga.addAll(tempList);
                                    categoryAdapter.notifyDataSetChanged(); // Notify the adapter here
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Failed to read manga data: " + databaseError.getMessage());
                                processedItems[0]++; // Ensure the counter progresses even if there's an error
                            }
                        });
                    } else {
                        // Handle cases where mangaId is null
                        processedItems[0]++;
                        if (processedItems[0] == totalItems) {
                            listHisManga.clear();
                            listHisManga.addAll(tempList);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read history data: " + databaseError.getMessage());
            }
        });
    }

}
