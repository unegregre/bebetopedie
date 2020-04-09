package com.gregre.bbtopdie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gregre.bbtopdie.bug.BugFragment;
import com.gregre.bbtopdie.bug.BugListAdapter;
import com.gregre.bbtopdie.bug.BugViewModel;
import com.gregre.bbtopdie.db.Bug;
import com.gregre.bbtopdie.db.Fish;
import com.gregre.bbtopdie.fish.FishListAdapter;
import com.gregre.bbtopdie.fish.FishViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeFragment extends Fragment {
    private BugViewModel mBugViewModel;
    private FishViewModel mFishViewModel;

    private static final String ARG_COUNT = "param1";
    private Integer counter;

    public TimeFragment() {
        // Required empty public constructor
    }
    public static TimeFragment newInstance(Integer counter) {
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int month = cal.get(Calendar.MONTH);

        RecyclerView bugRecyclerView = view.findViewById(R.id.bug_recyclerview);
        final BugListAdapter bugAdapter = new BugListAdapter(this.getContext());
        bugRecyclerView.setAdapter(bugAdapter);
        bugRecyclerView.setLayoutManager(new GridLayoutManager(bugRecyclerView.getContext(),2));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mBugViewModel = new ViewModelProvider(this).get(BugViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedBugs.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mBugViewModel.getBugsNow(hour, month).observe(getViewLifecycleOwner(), new Observer<List<Bug>>() {
            @Override
            public void onChanged(@Nullable final List<Bug> bugs) {
                // Update the cached copy of the bugs in the adapter.
                bugAdapter.setBugs(bugs);
            }
        });

        RecyclerView fishRecyclerView = view.findViewById(R.id.fish_recyclerview);
        final FishListAdapter fishAdapter = new FishListAdapter(this.getContext());
        fishRecyclerView.setAdapter(fishAdapter);
        fishRecyclerView.setLayoutManager(new GridLayoutManager(fishRecyclerView.getContext(),2));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mFishViewModel = new ViewModelProvider(this).get(FishViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedBugs.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mFishViewModel.getFishesNow(hour, month).observe(getViewLifecycleOwner(), new Observer<List<Fish>>() {
            @Override
            public void onChanged(@Nullable final List<Fish> fishes) {
                // Update the cached copy of the bugs in the adapter.
                fishAdapter.setFishes(fishes);
            }
        });

    }

}
