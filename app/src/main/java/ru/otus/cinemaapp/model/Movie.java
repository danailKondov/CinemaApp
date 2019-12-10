package ru.otus.cinemaapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    @Ignore
    public Movie(@NonNull Integer id, String overview, String title, boolean isRemarkable) {
        this.id = id;
        this.overview = overview;
        this.title = title;
        this.isRemarkable = isRemarkable;
    }

    public Movie() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
