package com.gregre.bbtopdie.db;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.StringJoiner;

@Entity(tableName = "bug_table")
public class Bug {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "bug_id")
    private int id;

    @NonNull
    @ColumnInfo(name = "bug_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "bug_price")
    private int price;

    @NonNull
    @ColumnInfo(name = "bug_location")
    private String location;

    @NonNull
    @ColumnInfo(name = "bug_is_all_day")
    private boolean is_all_day;

    @NonNull
    @ColumnInfo(name = "bug_is_all_year")
    private boolean is_all_year;

    @NonNull
    @ColumnInfo(name="bug_rarity")
    private String rarity;

    @NonNull
    @ColumnInfo(name = "bug_time")
    private String time;

    @NonNull
    @ColumnInfo(name = "bug_period_north")
    private String period_north;

    @NonNull
    @ColumnInfo(name = "bug_period_south")
    private String period_south;

    @NonNull
    @ColumnInfo(name = "bug_icon_uri")
    private String icon_uri;

    public Bug(@NonNull int id, @NonNull String name, @NonNull String period_north, @NonNull String period_south,
               @NonNull String time, @NonNull boolean is_all_day,
               @NonNull boolean is_all_year, @NonNull String location, @NonNull String rarity,
               @NonNull int price, @NonNull String icon_uri) {
        this.id = id;
        this.name = name;
        this.period_north = period_north;
        this.period_south = period_south;
        this.time = time;
        this.is_all_day = is_all_day;
        this.is_all_year = is_all_year;
        this.location = location;
        this.rarity = rarity;
        this.price = price;
        this.icon_uri = icon_uri;
    }

    public Bug(@NonNull int id, @NonNull String name, @NonNull List<Integer> period_north, @NonNull List<Integer> period_south,
               @NonNull List<Integer> time, @NonNull boolean is_all_day,
               @NonNull boolean is_all_year, @NonNull String location, @NonNull String rarity,
               @NonNull int price, @NonNull String icon_uri) {
        this.id = id;
        this.name = name;
        this.period_north = ";";
        for (int i : period_north) {
            this.period_north += i + ";";
        }
        this.period_south = ";";
        for (int i : period_south) {
            this.period_south += i + ";";
        }
        this.time = ";";
        for (int i : time) {
            this.time += i + ";";
        }
        this.is_all_day = is_all_day;
        this.is_all_year = is_all_year;
        this.location = location;
        this.rarity = rarity;
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

    public String getLocation(){return this.location;}

    public String getRarity() {return this.rarity;}

    public int getPrice(){return this.price;}

    public String getIcon_uri(){return this.icon_uri;}
}