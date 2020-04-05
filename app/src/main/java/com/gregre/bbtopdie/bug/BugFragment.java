package com.gregre.bbtopdie.bug;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.Bug;

import java.util.List;

public class BugFragment extends Fragment {

    private BugViewModel mBugViewModel;

    private static final String ARG_COUNT = "param1";
    private Integer counter;
    private int[] COLOR_MAP = {
            R.color.red_100, R.color.red_300, R.color.red_500, R.color.red_700, R.color.blue_100,
            R.color.blue_300, R.color.blue_500, R.color.blue_700, R.color.green_100, R.color.green_300,
            R.color.green_500, R.color.green_700
    };
    public BugFragment() {
        // Required empty public constructor
    }
    public static BugFragment newInstance(Integer counter) {
        BugFragment fragment = new BugFragment();
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
        return inflater.inflate(R.layout.fragment_bug, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));

        RecyclerView bugRecyclerView = view.findViewById(R.id.bug_recyclerview);
        final BugListAdapter bugAdapter = new BugListAdapter(this.getContext());
        bugRecyclerView.setAdapter(bugAdapter);
        bugRecyclerView.setLayoutManager(new LinearLayoutManager(bugRecyclerView.getContext()));

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
    }
}
