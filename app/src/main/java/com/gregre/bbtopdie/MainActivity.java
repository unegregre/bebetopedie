package com.gregre.bbtopdie;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_BUG_ACTIVITY_REQUEST_CODE = 1;
    public static final int[] TABS_ICON = {R.drawable.ic_bug_yellow,R.drawable.ic_time_bug, R.drawable.ic_fish_yellow,R.drawable.ic_time_fish, R.drawable.ic_fish_yellow,R.drawable.ic_time_fish};
    public static final int[] TABS_LABEL = {R.string.tab0,R.string.tab1,R.string.tab2};

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
            }
        });

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(createCardAdapter());

        // Linking with the tab
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
//                    tab.setText(TABS_LABEL[position]);
                    tab.setIcon(TABS_ICON[position * 2]);
                    //TODO: find a way to write in multiline or remove the label
                }
        ).attach();



        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.NH_yellow));
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.NH_white), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.NH_gray), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.NH_gray), PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.NH_white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.NH_gray), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BUG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void changeTabsIcon(int index, boolean isAll) {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        if(isAll) {
            tab.setIcon(TABS_ICON[index * 2 + 1]);
        } else {
            tab.setIcon(TABS_ICON[index * 2]);
        }
        if(tab.isSelected()) {
            tab.getIcon().setColorFilter(getResources().getColor(R.color.NH_white), PorterDuff.Mode.SRC_IN);
        } else {
            tab.getIcon().setColorFilter(getResources().getColor(R.color.NH_gray), PorterDuff.Mode.SRC_IN);
        }
    }
}