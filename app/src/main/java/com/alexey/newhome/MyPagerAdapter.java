package com.alexey.newhome;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class MyPagerAdapter extends FragmentStateAdapter {
    public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DayFragment();
            case 1:
                return new MonthFragment();
            case 2:
                return new YearFragment();
            default:
                return new DayFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
