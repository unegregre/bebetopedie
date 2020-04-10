package com.gregre.bbtopdie;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gregre.bbtopdie.bug.BugViewModel;
import com.gregre.bbtopdie.db.Bug;
import com.gregre.bbtopdie.fish.FishViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_BUG_ACTIVITY_REQUEST_CODE = 1;
    public static final int[] TABS_LABEL = {R.string.tab0, R.string.tab1};

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(createCardAdapter());

        // Linking with the tab
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(TABS_LABEL[position])
        ).attach();
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
}