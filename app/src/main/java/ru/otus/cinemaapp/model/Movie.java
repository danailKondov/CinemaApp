package ru.otus.cinemaapp.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    public Integer id;

    @SerializedName("poster_path")
    public String posterPath;

    public String overview;

    @SerializedName("original_title")
    public String originalTitle;

    public String title;
}
