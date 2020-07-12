package com.gregre.bbtopdie.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FishDao {

    @Query("SELECT * from fish_table ORDER BY fish_id ASC")
    LiveData<List<Fish>> getAllFishes();

    @Query("SELECT * from fish_table WHERE " +
            "fish_period_north_array LIKE :month " +
            "AND " +
            "fish_time_array LIKE :hour")
    LiveData<List<Fish>> getFishesNow(String hour, String month);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Fish fish);

    @Query("DELETE FROM fish_table")
    void deleteAll();
}
