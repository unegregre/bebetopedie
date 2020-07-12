package com.gregre.bbtopdie.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

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
    @ColumnInfo(name = "fish_name_fr")
    private String name_fr;

    @NonNull
    @ColumnInfo(name = "fish_location")
    private String location;

    @NonNull
    @ColumnInfo(name = "fish_shadow")
    private String shadow;

    @NonNull
    @ColumnInfo(name = "fish_is_all_day")
    private boolean is_all_day;

    @NonNull
    @ColumnInfo(name = "fish_is_all_year")
    private boolean is_all_year;

    @NonNull
    @ColumnInfo(name="bug_rarity")
    private String rarity;

    @NonNull
    @ColumnInfo(name = "fish_time")
    private String time;

    @NonNull
    @ColumnInfo(name = "fish_time_array")
    private String time_array;

    @NonNull
    @ColumnInfo(name = "fish_period_north")
    private String period_north;

    @NonNull
    @ColumnInfo(name = "fish_period_north_array")
    private String period_north_array;

    @NonNull
    @ColumnInfo(name = "fish_period_south")
    private String period_south;

    @NonNull
    @ColumnInfo(name = "fish_period_south_array")
    private String period_south_array;

    @NonNull
    @ColumnInfo(name = "fish_price")
    private int price;

    @NonNull
    @ColumnInfo(name = "fish_icon_uri")
    private String icon_uri;


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
    public Fish(@NonNull int id, @NonNull String name, @NonNull String name_fr, @NonNull String period_north, @NonNull String period_north_array, @NonNull String period_south,
                @NonNull String period_south_array, @NonNull String time, @NonNull String time_array, @NonNull boolean is_all_day,
                @NonNull boolean is_all_year, @NonNull String location, @NonNull String rarity,
                @NonNull int price, @NonNull String shadow, @NonNull String icon_uri) {
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
        this.location = location;
        this.rarity = rarity;
        this.price = price;
        this.shadow = shadow;
        this.icon_uri = icon_uri;
    }

    public Fish(@NonNull int id, @NonNull String name, @NonNull String name_fr, @NonNull String period_north, @NonNull List<Integer> period_north_array, @NonNull String period_south, @NonNull List<Integer> period_south_array,
                @NonNull String time, @NonNull List<Integer> time_array, @NonNull boolean is_all_day,
                @NonNull boolean is_all_year, @NonNull String location, @NonNull String rarity,
                @NonNull int price, @NonNull String shadow, @NonNull String icon_uri) {
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
        this.location = location;
        this.rarity = rarity;
        this.price = price;
        this.shadow = shadow;
        this.icon_uri = icon_uri;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getName_fr() {
        return name_fr;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    @NonNull
    public String getShadow() {
        return shadow;
    }

    public boolean isIs_all_day() {
        return is_all_day;
    }

    public boolean isIs_all_year() {
        return is_all_year;
    }

    @NonNull
    public String getRarity() {
        return rarity;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    @NonNull
    public String getTime_array() {
        return time_array;
    }

    @NonNull
    public String getPeriod_north() {
        return period_north;
    }

    @NonNull
    public String getPeriod_north_array() {
        return period_north_array;
    }

    @NonNull
    public String getPeriod_south() {
        return period_south;
    }

    @NonNull
    public String getPeriod_south_array() {
        return period_south_array;
    }

    public int getPrice() {
        return price;
    }

    @NonNull
    public String getIcon_uri() {
        return icon_uri;
    }
}
