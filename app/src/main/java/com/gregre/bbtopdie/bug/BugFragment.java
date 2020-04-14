package com.gregre.bbtopdie.bug;

import android.content.res.ColorStateList;
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
import com.gregre.bbtopdie.MainActivity;
import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.Bug;

import java.util.Calendar;
import java.util.List;

public class BugFragment extends Fragment {

    private BugViewModel mBugViewModel;
    private boolean allBugs;
    private static final String ARG_COUNT = "param1";

    public BugFragment() {
        // Required empty public constructor
    }
    public static BugFragment newInstance(Integer counter) {
        BugFragment fragment = new BugFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        fragment.setAllBugs(true);
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
        return inflater.inflate(R.layout.fragment_bug, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView bugRecyclerView = view.findViewById(R.id.bug_recyclerview);
        final BugListAdapter bugAdapter = new BugListAdapter(this.getContext());
        bugRecyclerView.setAdapter(bugAdapter);
        bugRecyclerView.setLayoutManager(new GridLayoutManager(bugRecyclerView.getContext(),2));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mBugViewModel = new ViewModelProvider(this).get(BugViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedBugs.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mBugViewModel.getAllBugs().observe(getViewLifecycleOwner(), new Observer<List<Bug>>() {
            @Override
            public void onChanged(@Nullable final List<Bug> bugs) {
                // Update the cached copy of the bugs in the adapter.
                bugAdapter.setBugs(bugs);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.bugFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allBugs) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_apps_black_24dp));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.NH_blue)));
                } else {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_access_time_white_24dp));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.NH_red)));
                }
                MainActivity main = (MainActivity) getActivity();
                main.changeTabsIcon(0,allBugs);
                changeBugs(bugAdapter);
            }
        });

    }

    public void changeBugs(BugListAdapter bugAdapter) {
        if(allBugs) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int month = cal.get(Calendar.MONTH) + 1;
            mBugViewModel.getBugsNow(hour,month).observe(getViewLifecycleOwner(), new Observer<List<Bug>>() {
                @Override
                public void onChanged(@Nullable final List<Bug> bugs) {
                    // Update the cached copy of the bugs in the adapter.
                    bugAdapter.setBugs(bugs);
                }
            });
            setAllBugs(false);
        } else {
            mBugViewModel.getAllBugs().observe(getViewLifecycleOwner(), new Observer<List<Bug>>() {
                @Override
                public void onChanged(@Nullable final List<Bug> bugs) {
                    // Update the cached copy of the bugs in the adapter.
                    bugAdapter.setBugs(bugs);
                }
            });
            setAllBugs(true);
        }
    }

    public void setAllBugs(boolean bool) {
        allBugs = bool;
    }
}
