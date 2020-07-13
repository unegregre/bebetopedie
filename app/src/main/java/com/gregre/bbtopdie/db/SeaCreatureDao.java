package com.gregre.bbtopdie.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
public interface SeaCreatureDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from sea_table ORDER BY sea_id ASC")
    LiveData<List<SeaCreature>> getAllSeaCreatures();

    @Query("SELECT * from sea_table WHERE " +
            "sea_period_north_array LIKE :month " +
            "AND " +
            "sea_time_array LIKE :hour")
    LiveData<List<SeaCreature>> getSeaCreaturesNow(String hour, String month);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SeaCreature seaCreature);

    @Query("DELETE FROM sea_table")
    void deleteAll();
}