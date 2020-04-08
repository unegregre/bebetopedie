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

@Database(entities = {Bug.class, Fish.class}, version = 5, exportSchema = false)
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

                Bug bug = new Bug("Papillon",200, 9, 16, 3, 6);
                bug_dao.insert(bug);
                bug = new Bug("Fourmi",100, 0,0, 10, 3);
                bug_dao.insert(bug);

                FishDao fish_dao = INSTANCE.fishDao();
                fish_dao.deleteAll();

                Fish fish = new Fish("Bouvière", 900,0,0,11,3,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Chevaine", 200, 9,16,0,0,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Carassin",160,0,0,0,0,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Vandoise", 240, 16,9,0,0,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Carpe",300, 0,0, 0,0,"Étang");
                fish_dao.insert(fish);
                fish = new Fish("Carpe Koi", 4000,16,4,0,0,"Étang");
                fish_dao.insert(fish);
                fish = new Fish("Poisson rouge", 1300, 0,0,0,0,"Étang");
                fish_dao.insert(fish);
                fish = new Fish("Cyprin doré", 1300, 9, 16, 0,0, "Étang");
                fish_dao.insert(fish);
                fish = new Fish("Ranchu", 4500, 9,16,0,0, "Étang");
                fish_dao.insert(fish);
                fish = new Fish("Fondule barré", 300, 0,0,4,8,"Étang");
                fish_dao.insert(fish);
                fish = new Fish("Homard", 200, 0,0, 4, 9, "Étang");
                fish_dao.insert(fish);
                fish = new Fish("Tortue trionyx", 3750, 16, 9, 8,9,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Tortue serpentine", 5000, 21, 4, 4, 10, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Tétard", 100, 0, 0, 3, 7, "Étang");
                fish_dao.insert(fish);
                fish = new Fish("Grenouille", 120, 0, 0, 5, 8, "Étang");
                fish_dao.insert(fish);
                fish = new Fish("Gobie d'eau douce", 400, 16, 9, 0, 0, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Loche d'étang", 400, 0,0, 3,5, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Poisson-chat", 800, 16, 9, 5, 10, "Étang");
                fish_dao.insert(fish);
                fish = new Fish("Tête de serpent", 5500, 9, 16, 6, 8, "Étang");
                fish_dao.insert(fish);
                fish = new Fish("Crapet", 180, 9, 16, 0,0,"Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Perche jaune", 300, 0,0, 10, 3, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Bar", 400, 0,0, 0,0, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Tilapia", 800, 0,0,6, 10, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Brochet", 1800, 0, 0, 9, 12, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Éperlan", 500, 0,0, 12, 2, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Ayu", 900, 0, 0, 7, 9, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Saumon masou", 1000, 16,9,3,11,"Rivière (falaise)");
                fish_dao.insert(fish);
                fish = new Fish("Omble", 3800, 16, 9, 3, 11, "Rivière (falaise)");
                fish_dao.insert(fish);
                fish = new Fish("Truite dorée", 15000, 16,9, 3, 11, "Rivière (falaise)");
                fish_dao.insert(fish);
                fish = new Fish("Dai yu", 15000, 16,9,12,3,"Rivière (falaise)");
                fish_dao.insert(fish);
                fish = new Fish("Saumon", 700, 0,0, 9,9,"Rivière (embouchure)");
                fish_dao.insert(fish);
                fish = new Fish("Saumon roi", 1800, 0,0, 9,9, "Rivière (embouchure)");
                fish_dao.insert(fish);
                fish = new Fish("Crabe chinois", 2000, 16, 9, 9, 11, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Guppy", 1300, 9, 16, 4, 11, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Poisson docteur", 1500, 9, 16, 5, 9, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Poisson-ange", 300, 16, 9, 5, 10, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Combattant", 2500, 9,16, 5, 11, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Néon bleu", 500, 9,16, 4, 11, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Poisson arc-en-ciel", 800, 9, 16, 5, 10, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Piranha", 2500, 9, 16, 6, 9, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Arowana", 10000, 16, 9, 6, 9, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Dorade", 15000, 4, 21, 6, 9, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Gar", 6000, 16, 9, 6, 9, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Arapaima", 10000, 16, 9, 6, 9, "Rivière");
                fish_dao.insert(fish);
                fish = new Fish("Bichir", 4000, 21, 4, 6, 9, "Rivière");
                fish_dao.insert(fish);

            });
        }
    };
}