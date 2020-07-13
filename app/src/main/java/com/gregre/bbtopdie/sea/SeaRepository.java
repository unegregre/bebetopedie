package com.gregre.bbtopdie.sea;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gregre.bbtopdie.db.BebeteDatabase;
import com.gregre.bbtopdie.db.SeaCreature;
import com.gregre.bbtopdie.db.SeaCreatureDao;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

class SeaRepository {

    private SeaCreatureDao mSeaCreatureDao;
    private LiveData<List<SeaCreature>> mAllSeaCreatures;

    // Note that in order to unit test the SeaCreatureRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    SeaRepository(Application application) {
        BebeteDatabase db = BebeteDatabase.getDatabase(application);
        mSeaCreatureDao = db.seaCreatureDao();
        mAllSeaCreatures = mSeaCreatureDao.getAllSeaCreatures();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<SeaCreature>> getAllSeaCreatures() {
        return mAllSeaCreatures;
    }

    LiveData<List<SeaCreature>> getSeaCreaturesNow(int hour, int month) {
        String h = "%;"+hour+";%";
        String m = "%;"+month+";%";
        return mSeaCreatureDao.getSeaCreaturesNow(h, m);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(SeaCreature seaCreature) {
        BebeteDatabase.databaseWriteExecutor.execute(() -> {
            mSeaCreatureDao.insert(seaCreature);
        });
    }
}

