package com.gregre.bbtopdie.fish;

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
import com.gregre.bbtopdie.db.Fish;

import java.util.List;

public class FishFragment extends Fragment{

    private FishViewModel mFishViewModel;

    private static final String ARG_COUNT = "param1";

    public FishFragment() {
        // Required empty public constructor
    }
    public static FishFragment newInstance(Integer counter) {
        FishFragment fragment = new FishFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
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
        fishRecyclerView.setLayoutManager(new LinearLayoutManager(fishRecyclerView.getContext()));

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
    }
}
