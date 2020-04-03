package com.gregre.bbtopdie;

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

@Database(entities = {Bug.class}, version = 2, exportSchema = false)
abstract class BebeteDatabase extends RoomDatabase {

    abstract BugDao bugDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile BebeteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static BebeteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BebeteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BebeteDatabase.class, "bug_database")
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
                BugDao dao = INSTANCE.bugDao();
                dao.deleteAll();

                Bug bug = new Bug("Papillon","200", "12h", "Printemps");
                dao.insert(bug);
                bug = new Bug("Fourmi","100", "12h", "Printemps");
                dao.insert(bug);
            });
        }
    };
}