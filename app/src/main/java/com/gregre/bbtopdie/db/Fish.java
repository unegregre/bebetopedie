package com.gregre.bbtopdie.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fish_table")
public class Fish {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fish_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "fish_price")
    private String price;

    @NonNull
    @ColumnInfo(name = "fish_time")
    private String time;

    @NonNull
    @ColumnInfo(name = "fish_period")
    private String period;

    @NonNull
    @ColumnInfo(name = "fish_place")
    private String place;

    public Fish(@NonNull String name, @NonNull String price, @NonNull String time, @NonNull String period, @NonNull String place) {
        this.name = name;
        this.price = price;
        this.time = time;
        this.period = period;
        this.place = place;
    }

    public String getName(){return this.name;}

    public String getPrice(){return this.price;}

    public String getTime(){return this.time;}

    public String getPeriod(){return this.period;}

    public String getPlace(){return this.place;}

}
