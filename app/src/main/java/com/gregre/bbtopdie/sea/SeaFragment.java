package com.gregre.bbtopdie.sea;

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
import com.gregre.bbtopdie.db.SeaCreature;

import java.util.Calendar;
import java.util.List;

public class SeaFragment extends Fragment {

    private SeaViewModel mSeaViewModel;
    private boolean allSeaCreatures;
    private static final String ARG_COUNT = "param1";

    public SeaFragment() {
        // Required empty public constructor
    }
    public static SeaFragment newInstance(Integer counter) {
        SeaFragment fragment = new SeaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        fragment.setAllSeaCreatures(true);
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
        return inflater.inflate(R.layout.fragment_sea, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView seaRecyclerView = view.findViewById(R.id.sea_recyclerview);
        final SeaListAdapter seaAdapter = new SeaListAdapter(this.getContext());
        seaRecyclerView.setAdapter(seaAdapter);
        seaRecyclerView.setLayoutManager(new GridLayoutManager(seaRecyclerView.getContext(),2));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mSeaViewModel = new ViewModelProvider(this).get(SeaViewModel.class);

        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mSeaViewModel.getAllSeaCreatures().observe(getViewLifecycleOwner(), new Observer<List<SeaCreature>>() {
            @Override
            public void onChanged(@Nullable final List<SeaCreature> seaCreatures) {
                seaAdapter.setSeaCreatures(seaCreatures);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.seaFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allSeaCreatures) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_apps_black_24dp));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.NH_blue)));
                } else {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_access_time_white_24dp));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.NH_red)));
                }
                MainActivity main = (MainActivity) getActivity();
                main.changeTabsIcon(2, allSeaCreatures);
                changeSeaCreatures(seaAdapter);
            }
        });

    }

    public void changeSeaCreatures(SeaListAdapter seaAdapter) {
        if(allSeaCreatures) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int month = cal.get(Calendar.MONTH) + 1;
            mSeaViewModel.getSeaCreaturesNow(hour,month).observe(getViewLifecycleOwner(), new Observer<List<SeaCreature>>() {
                @Override
                public void onChanged(@Nullable final List<SeaCreature> seaCreatures) {
                    seaAdapter.setSeaCreatures(seaCreatures);
                }
            });
            setAllSeaCreatures(false);
        } else {
            mSeaViewModel.getAllSeaCreatures().observe(getViewLifecycleOwner(), new Observer<List<SeaCreature>>() {
                @Override
                public void onChanged(@Nullable final List<SeaCreature> seaCreatures) {
                    seaAdapter.setSeaCreatures(seaCreatures);
                }
            });
            setAllSeaCreatures(true);
        }
    }

    public void setAllSeaCreatures(boolean bool) {
        allSeaCreatures = bool;
    }
}
