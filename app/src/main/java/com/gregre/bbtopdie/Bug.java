package com.gregre.bbtopdie;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bug_table")
public class Bug {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "bug_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "bug_price")
    private String price;

    @NonNull
    @ColumnInfo(name = "bug_time")
    private String time;

    @NonNull
    @ColumnInfo(name = "bug_period")
    private String period;

    public Bug(@NonNull String name, @NonNull String price, @NonNull String time, @NonNull String period) {
        this.name = name;
        this.price = price;
        this.time = time;
        this.period = period;
    }

    String getName(){return this.name;}

    String getPrice(){return this.price;}

    String getTime(){return this.time;}

    String getPeriod(){return this.period;}
}