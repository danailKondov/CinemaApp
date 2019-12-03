package ru.otus.cinemaapp.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey
    @NonNull
    public Integer id;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath;

    public String overview;

    public String title;

    @ColumnInfo(name = "is_remarkable")
    public boolean isRemarkable;
}
