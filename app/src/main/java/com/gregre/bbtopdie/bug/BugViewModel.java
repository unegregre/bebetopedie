package com.gregre.bbtopdie.bug;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gregre.bbtopdie.db.Bug;

import java.util.List;

/**
 * View Model to keep a reference to the bug repository and
 * an up-to-date list of all bugs.
 */

public class BugViewModel extends AndroidViewModel {

    private BugRepository mRepository;
    // Using LiveData and caching what getAlphabetizedBugs returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Bug>> mAllBugs;

    private LiveData<List<Bug>> mBugsNow;

    public BugViewModel(Application application) {
        super(application);
        mRepository = new BugRepository(application);
        mAllBugs = mRepository.getAllBugs();
        mBugsNow = mRepository.getBugsNow();
    }

    public LiveData<List<Bug>> getAllBugs() {
        return mAllBugs;
    }

    public LiveData<List<Bug>> getBugsNow() {
        return mBugsNow;
    }

    public void insert(Bug bug) {
        mRepository.insert(bug);
    }
}