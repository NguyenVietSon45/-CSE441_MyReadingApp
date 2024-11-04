package com.example.myreadingapp.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myreadingapp.Adapter.ViewPagerAdapter;
import com.example.myreadingapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MangaLibraryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manga_library, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        // Set up ViewPager2 with the adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        // Attach TabLayout to ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Yêu thích"); // Favorites
                        break;
                    case 1:
                        tab.setText("Lịch sử"); // History
                        break;
                }
            }
        }).attach();

        return view;
    }
}

