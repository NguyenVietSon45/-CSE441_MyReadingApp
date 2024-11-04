package com.example.myreadingapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myreadingapp.Activities.FavoriteFragment;
import com.example.myreadingapp.Activities.HistoryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FavoriteFragment(); // First tab (Favorites)
            case 1:
                return new HistoryFragment(); // Second tab (History)
            default:
                return new FavoriteFragment(); // Default (Favorites)
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}

