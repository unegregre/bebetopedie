package com.gregre.bbtopdie.bug;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.gregre.bbtopdie.db.BebeteDatabase;
import com.gregre.bbtopdie.db.Bug;
import com.gregre.bbtopdie.db.BugDao;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

class BugRepository {

    private BugDao mBugDao;
    private LiveData<List<Bug>> mAllBugs;

    // Note that in order to unit test the BugRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    BugRepository(Application application) {
        BebeteDatabase db = BebeteDatabase.getDatabase(application);
        mBugDao = db.bugDao();
        mAllBugs = mBugDao.getAllBugs();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Bug>> getAllBugs() {
        return mAllBugs;
    }

    LiveData<List<Bug>> getBugsNow(int hour, int month) {
        String h = "%;"+hour+";%";
        String m = "%;"+month+";%";
        return mBugDao.getBugsNow(h, m);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Bug bug) {
        BebeteDatabase.databaseWriteExecutor.execute(() -> {
            mBugDao.insert(bug);
        });
    }
}

