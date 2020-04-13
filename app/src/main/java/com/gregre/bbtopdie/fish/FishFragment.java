package com.gregre.bbtopdie.fish;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.gregre.bbtopdie.MainActivity;
import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.Fish;

import java.util.Calendar;
import java.util.List;

public class FishFragment extends Fragment{

    private FishViewModel mFishViewModel;
    private boolean allFishes;
    private static final String ARG_COUNT = "param1";

    public FishFragment() {
        // Required empty public constructor
    }
    public static FishFragment newInstance(Integer counter) {
        FishFragment fragment = new FishFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        fragment.setAllFishes(true);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fish, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView fishRecyclerView = view.findViewById(R.id.fish_recyclerview);
        final FishListAdapter fishAdapter = new FishListAdapter(this.getContext());
        fishRecyclerView.setAdapter(fishAdapter);
        fishRecyclerView.setLayoutManager(new GridLayoutManager(fishRecyclerView.getContext(), 2));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mFishViewModel = new ViewModelProvider(this).get(FishViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedBugs.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mFishViewModel.getAllFishes().observe(getViewLifecycleOwner(), new Observer<List<Fish>>() {
            @Override
            public void onChanged(@Nullable final List<Fish> fishes) {
                // Update the cached copy of the bugs in the adapter.
                fishAdapter.setFishes(fishes);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fishFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allFishes) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_apps_black_24dp));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.NH_blue)));

                } else {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_access_time_white_24dp));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.NH_red)));
                }
                MainActivity main = (MainActivity) getActivity();
                main.changeTabsIcon(1,allFishes);
                changeFishes(fishAdapter);
            }
        });
    }

    public void changeFishes(FishListAdapter fishAdapter) {
        if(allFishes) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int month = cal.get(Calendar.MONTH) + 1;
            System.out.println("MOIS : "+ month);
            System.out.println("MOIS : "+ month);
            System.out.println("MOIS : "+ month);
            mFishViewModel.getFishesNow(hour,month).observe(getViewLifecycleOwner(), new Observer<List<Fish>>() {
                @Override
                public void onChanged(@Nullable final List<Fish> fishes) {
                    // Update the cached copy of the bugs in the adapter.
                    fishAdapter.setFishes(fishes);
                }
            });
            setAllFishes(false);
        } else {
            mFishViewModel.getAllFishes().observe(getViewLifecycleOwner(), new Observer<List<Fish>>() {
                @Override
                public void onChanged(@Nullable final List<Fish> fishes) {
                    // Update the cached copy of the bugs in the adapter.
                    fishAdapter.setFishes(fishes);
                }
            });
            setAllFishes(true);
        }
    }

    public void setAllFishes(boolean bool) {
        allFishes = bool;
    }
}
