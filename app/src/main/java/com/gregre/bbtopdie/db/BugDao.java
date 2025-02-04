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
public interface BugDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from bug_table ORDER BY bug_id ASC")
    LiveData<List<Bug>> getAllBugs();

    @Query("SELECT * from bug_table WHERE " +
            "bug_period_north_array LIKE :month " +
            "AND " +
            "bug_time_array LIKE :hour")
    LiveData<List<Bug>> getBugsNow(String hour, String month);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Bug bug);

    @Query("DELETE FROM bug_table")
    void deleteAll();
}