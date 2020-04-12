package com.gregre.bbtopdie.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fish_table")
public class Fish {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fish_id")
    private int id;

    @NonNull
    @ColumnInfo(name = "fish_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "fish_price")
    private int price;

    @NonNull
    @ColumnInfo(name = "fish_place")
    private String place;


    @NonNull
    @ColumnInfo(name = "fish_size")
    private int size;

    @NonNull
    @ColumnInfo(name = "fish_time_1")
    private int time_1;

    @NonNull
    @ColumnInfo(name = "fish_time_2")
    private int time_2;

    @NonNull
    @ColumnInfo(name = "fish_period_1")
    private int period_1;

    @NonNull
    @ColumnInfo(name = "fish_period_2")
    private int period_2;


    /*
      fish_id       INTEGER  NOT NULL PRIMARY KEY
  ,fish_name     VARCHAR(20) NOT NULL
  ,fish_price    INTEGER  NOT NULL
  ,fish_place    VARCHAR(25) NOT NULL
  ,fish_size     VARCHAR(7) NOT NULL
  ,fish_time_1   INTEGER  NOT NULL
  ,fish_time_2   INTEGER  NOT NULL
  ,fish_period_1 INTEGER  NOT NULL
  ,fish_period_2 INTEGER  NOT NULL
     */

    public Fish(@NonNull int id, @NonNull String name, @NonNull int price,  @NonNull String place, @NonNull int size, @NonNull int time_1, @NonNull int time_2, @NonNull int period_1, @NonNull int period_2) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.time_1 = time_1;
        this.time_2 = time_2;
        this.period_1 = period_1;
        this.period_2 = period_2;
        this.place = place;
        this.size = size;
    }

    public int getId(){return this.id;}

    public String getName(){return this.name;}

    public int getPrice(){return this.price;}

    public int getTime_1(){return this.time_1;}

    public int getTime_2(){return this.time_2;}

    public int getPeriod_1(){return this.period_1;}

    public int getPeriod_2() {return this.period_2;}

    public String getPlace(){return this.place;}

    public int getSize(){return this.size;}

}
