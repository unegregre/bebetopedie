package com.gregre.bbtopdie.fish;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gregre.bbtopdie.db.Fish;

import java.util.List;

public class FishViewModel extends AndroidViewModel {

    private FishRepository mRepository;
    // Using LiveData and caching what getAlphabetizedBugs returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Fish>> mAllFishes;

    public FishViewModel(Application application) {
        super(application);
        mRepository = new FishRepository(application);
        mAllFishes = mRepository.getAllFishes();
    }

    public LiveData<List<Fish>> getAllFishes() {
        return mAllFishes;
    }

    public LiveData<List<Fish>> getFishesNow(int hour, int month) {
        return mRepository.getFishesNow(hour, month);
    }

    public void insert(Fish fish) {
        mRepository.insert(fish);
    }
}
