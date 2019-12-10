package ru.otus.cinemaapp.fragments;

import java.util.List;

import androidx.lifecycle.LiveData;
import ru.otus.cinemaapp.model.Movie;

public interface FilmListFragmentInt {

    void addFilmToRemarkableList(int position);

    void addMovieListObserver(LiveData<List<Movie>> moviesFromDB);

    void showMoviesList(List<Movie> movies);

    void showRemarkableFilmsList(List<Integer> remarkableFilmsIdsList, boolean isRemarkable);
}
