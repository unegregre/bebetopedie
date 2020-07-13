package com.gregre.bbtopdie.sea;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gregre.bbtopdie.db.SeaCreature;

import java.util.List;


public class SeaViewModel extends AndroidViewModel {

    private SeaRepository mRepository;
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<SeaCreature>> mAllSeaCreatures;

    public SeaViewModel(Application application) {
        super(application);
        mRepository = new SeaRepository(application);
        mAllSeaCreatures = mRepository.getAllSeaCreatures();
    }

    public LiveData<List<SeaCreature>> getAllSeaCreatures() {
        return mAllSeaCreatures;
    }

    public LiveData<List<SeaCreature>> getSeaCreaturesNow(int hour, int month) {
        return mRepository.getSeaCreaturesNow(hour, month);
    }

    public void insert(SeaCreature seaCreature) {
        mRepository.insert(seaCreature);
    }
}