package com.gregre.bbtopdie.db;

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
    private int price;

    @NonNull
    @ColumnInfo(name = "bug_time_1")
    private int time_1;

    @NonNull
    @ColumnInfo(name = "bug_time_2")
    private int time_2;

    @NonNull
    @ColumnInfo(name = "bug_period_1")
    private int period_1;

    @NonNull
    @ColumnInfo(name = "bug_period_2")
    private int period_2;

    public Bug(@NonNull String name, @NonNull int price, @NonNull int time_1, @NonNull int time_2, @NonNull int period_1, @NonNull int period_2) {
        this.name = name;
        this.price = price;
        this.time_1 = time_1;
        this.time_2 = time_2;
        this.period_1 = period_1;
        this.period_2 = period_2;
    }

    public String getName(){return this.name;}

    public int getPrice(){return this.price;}

    public int getTime_1(){return this.time_1;}

    public int getTime_2(){return this.time_2;}

    public int getPeriod_1(){return this.period_1;}

    public int getPeriod_2() {return this.period_2;}
}