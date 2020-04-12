package com.gregre.bbtopdie.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.  In a real
 * app, consider exporting the schema to help you with migrations.
 */

@Database(entities = {Bug.class, Fish.class}, version = 7, exportSchema = false)
public abstract class BebeteDatabase extends RoomDatabase {

    public abstract BugDao bugDao();
    public abstract  FishDao fishDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile BebeteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BebeteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BebeteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BebeteDatabase.class, "acnh_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more bugs, just add them.
                BugDao bug_dao = INSTANCE.bugDao();
                bug_dao.deleteAll();

                Bug bug = new Bug(1, "Papillon",200, 9, 16, 3, 6);
                bug_dao.insert(bug);
                bug = new Bug(2, "Fourmi",100, 0,0, 10, 3);
                bug_dao.insert(bug);

                FishDao fish_dao = INSTANCE.fishDao();
                fish_dao.deleteAll();

                Fish fish = new Fish(1, "Bouvière", 900,0,0,11,3,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish(2, "Chevaine", 200, 9,16,0,0,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish(3, "Carassin",160,0,0,0,0,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish(4, "Vandoise", 240, 16,9,0,0,"Rivière");
                fish_dao.insert(fish);


            });
        }
    };
}