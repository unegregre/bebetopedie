package com.gregre.bbtopdie.db;


import android.util.JsonReader;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;


@Entity(tableName = "sea_table")
public class SeaCreature {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sea_id")
    private int id;

    @NonNull
    @ColumnInfo(name = "sea_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "sea_name_fr")
    private String name_fr;

    @NonNull
    @ColumnInfo(name = "sea_price")
    private int price;

    @NonNull
    @ColumnInfo(name = "sea_speed")
    private String speed;

    @NonNull
    @ColumnInfo(name = "sea_is_all_day")
    private boolean is_all_day;

    @NonNull
    @ColumnInfo(name = "sea_is_all_year")
    private boolean is_all_year;

    @NonNull
    @ColumnInfo(name="sea_shadow")
    private String shadow;

    @NonNull
    @ColumnInfo(name = "sea_time")
    private String time;

    @NonNull
    @ColumnInfo(name = "sea_time_array")
    private String time_array;

    @NonNull
    @ColumnInfo(name = "sea_period_north")
    private String period_north;

    @NonNull
    @ColumnInfo(name = "sea_period_north_array")
    private String period_north_array;

    @NonNull
    @ColumnInfo(name = "sea_period_south")
    private String period_south;

    @NonNull
    @ColumnInfo(name = "sea_period_south_array")
    private String period_south_array;

    @NonNull
    @ColumnInfo(name = "sea_icon_uri")
    private String icon_uri;

    public SeaCreature(@NonNull int id, @NonNull String name, @NonNull String name_fr, @NonNull String period_north, @NonNull String period_north_array, @NonNull String period_south,
                       @NonNull String period_south_array, @NonNull String time, @NonNull String time_array, @NonNull boolean is_all_day,
                       @NonNull boolean is_all_year, @NonNull String speed, @NonNull String shadow,
                       @NonNull int price, @NonNull String icon_uri) {
        this.id = id;
        this.name = name;
        this.name_fr = name_fr;
        this.period_north = period_north;
        this.period_north_array = period_north_array;
        this.period_south = period_south;
        this.period_south_array = period_south_array;
        this.time = time;
        this.time_array = time_array;
        this.is_all_day = is_all_day;
        this.is_all_year = is_all_year;
        this.speed = speed;
        this.shadow = shadow;
        this.price = price;
        this.icon_uri = icon_uri;
    }

    public SeaCreature(@NonNull int id, @NonNull String name, @NonNull String name_fr, @NonNull String period_north, @NonNull List<Integer> period_north_array, @NonNull String period_south, @NonNull List<Integer> period_south_array,
                       @NonNull String time, @NonNull List<Integer> time_array, @NonNull boolean is_all_day,
                       @NonNull boolean is_all_year, @NonNull String speed, @NonNull String shadow,
                       @NonNull int price, @NonNull String icon_uri) {
        this.id = id;
        this.name = name;
        this.name_fr = name_fr;
        this.period_north = period_north;
        this.period_north_array = ";";
        for (int i : period_north_array) {
            this.period_north_array += i + ";";
        }
        this.period_south = period_south;
        this.period_south_array = ";";
        for (int i : period_south_array) {
            this.period_south_array += i + ";";
        }
        this.time = time;
        this.time_array = ";";
        for (int i : time_array) {
            this.time_array += i + ";";
        }
        this.is_all_day = is_all_day;
        this.is_all_year = is_all_year;
        this.speed = speed;
        this.shadow = shadow;
        this.price = price;
        this.icon_uri = icon_uri;
    }



    public int getId() {return this.id;}

    public String getName(){return this.name;}

    public String getPeriod_south(){return this.period_south;}

    public String getPeriod_north() {return this.period_north;}

    public String getTime(){return this.time;}

    public boolean isIs_all_day() {return this.is_all_day;}

    public boolean isIs_all_year() {return this.is_all_year;}

    public String getSpeed(){return this.speed;}

    public String getShadow() {return this.shadow;}

    public int getPrice(){return this.price;}

    public String getIcon_uri(){return this.icon_uri;}

    @NonNull
    public String getTime_array() {
        return time_array;
    }

    @NonNull
    public String getPeriod_north_array() {
        return period_north_array;
    }

    @NonNull
    public String getPeriod_south_array() {
        return period_south_array;
    }

    @NonNull
    public String getName_fr() {
        return name_fr;
    }
}