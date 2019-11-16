package ru.otus.cinemaapp.repo;

import android.app.Activity;

import java.util.List;
import java.util.Optional;

import ru.otus.cinemaapp.model.Movie;

public interface FilmRepositoryInt {

    Optional<Movie> getFilmById(Integer id);

    List<Integer> getRemarkableFilmsIdsList();

    void setRemarkableFilmsIds(List<Integer> remarkableFilmsIds);

    List<Movie> getMovieList();

    List<Movie> getRemarkableMovies();

    void setRemarkableMovies(List<Movie> remarkableMovies);

    void setActivity(Activity activity);
}
