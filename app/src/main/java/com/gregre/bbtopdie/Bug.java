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

    public Bug(@NonNull String name, @NonNull String price) {
        this.name = name;
        this.price = price;
    }

    String getName(){return this.name;}

    String getPrice(){return this.price;}
}