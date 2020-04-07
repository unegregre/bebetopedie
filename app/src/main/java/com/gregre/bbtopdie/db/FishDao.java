package com.gregre.bbtopdie.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FishDao {

    @Query("SELECT * from fish_table ORDER BY fish_name ASC")
    LiveData<List<Fish>> getAlphabetizedFish();

    @Query("SELECT * from fish_table WHERE fish_price < 150")
    LiveData<List<Fish>> getFishesNow();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Fish fish);

    @Query("DELETE FROM fish_table")
    void deleteAll();
}
