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
            "(( fish_time_1 = 0)" +
            "OR" +
            "(fish_time_1 < fish_time_2 AND :hour BETWEEN fish_time_1 AND fish_time_2) " +
            "OR " +
            "(fish_time_1 > fish_time_2 AND :hour NOT BETWEEN fish_time_2 AND fish_time_1)) " +
            "AND " +
            "((fish_period_1 = 0)" +
            "OR" +
            "(fish_period_1 < fish_period_2 AND :month BETWEEN fish_period_1 AND fish_period_2) " +
            "OR " +
            "(fish_period_1 > fish_period_2 AND :month NOT BETWEEN fish_period_2 AND fish_period_1))")
    LiveData<List<Fish>> getFishesNow(int hour, int month);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Fish fish);

    @Query("DELETE FROM fish_table")
    void deleteAll();
}
