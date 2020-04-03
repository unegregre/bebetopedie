package com.gregre.bbtopdie.fish;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gregre.bbtopdie.db.BebeteDatabase;
import com.gregre.bbtopdie.db.Fish;
import com.gregre.bbtopdie.db.FishDao;

import java.util.List;

public class FishRepository {

    private FishDao mFishDao;
    private LiveData<List<Fish>> mAllFishes;

    // Note that in order to unit test the BugRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    FishRepository(Application application) {
        BebeteDatabase db = BebeteDatabase.getDatabase(application);
        mFishDao = db.fishDao();
        mAllFishes = mFishDao.getAlphabetizedFish();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Fish>> getAllFishes() {
        return mAllFishes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Fish fish) {
        BebeteDatabase.databaseWriteExecutor.execute(() -> {
            mFishDao.insert(fish);
        });
    }
}
