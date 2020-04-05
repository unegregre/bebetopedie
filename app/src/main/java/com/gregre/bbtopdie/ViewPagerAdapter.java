package com.gregre.bbtopdie;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gregre.bbtopdie.bug.BugFragment;
import com.gregre.bbtopdie.fish.FishFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int TAB_COUNT = 3;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull @Override public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return BugFragment.newInstance(position);
            case 1:
                return FishFragment.newInstance(position);
            default:
                return BugFragment.newInstance(position);
        }
    }
    @Override public int getItemCount() {
        return TAB_COUNT;
    }
}