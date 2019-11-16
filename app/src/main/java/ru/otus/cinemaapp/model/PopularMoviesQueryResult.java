package ru.otus.cinemaapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularMoviesQueryResult {

    public Integer page;

    public List<Movie> results;

    @SerializedName("total_results")
    public Integer totalResults;

    @SerializedName("total_pages")
    public Integer totalPages;
}
