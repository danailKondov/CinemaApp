package ru.otus.cinemaapp.repo;

import android.app.Activity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import androidx.lifecycle.LiveData;
import ru.otus.cinemaapp.model.Movie;

public interface FilmRepositoryInt {

    void addMovies(List<Movie> movies);

    Optional<Movie> getFilmById(Integer id);

    List<Integer> getRemarkableFilmsIdsList();

    void setRemarkableFilmsIds(List<Integer> remarkableFilmsIds);

    List<Movie> getMovies();

    void setMovies(List<Movie> movies);

    List<Movie> getRemarkableMovies();

    void setRemarkableMovies(List<Movie> remarkableMovies);

    void setActivity(Activity activity);

    void initDB();

    LiveData<List<Movie>> getMoviesFromDB();

    void setFirstStartDate(LocalDate now);

    LocalDate getFirstStartDate();

    void updateAllMovies();
}
