package ru.otus.cinemaapp.repo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ru.otus.cinemaapp.model.Movie;

public class FilmRepository implements FilmRepositoryInt {

    private static final String SHARED_REMARKABLE_FILMS_IDS = "sharedRemarkableFilmsIds";

    private List<Integer> remarkableFilmsIds;

    private List<Movie> movies = new ArrayList<>();

    private List<Movie> remarkableMovies = Collections.synchronizedList(new ArrayList<>());

    private Activity activity;

    private static FilmRepository instance;

    private FilmRepository() {
    }

    public static FilmRepository getInstance() {
        if (instance == null) {
            instance = new FilmRepository();
        }
        return instance;
    }

    @Override
    public Optional<Movie> getFilmById(Integer id) {
        return movies
                .stream()
                .filter(x -> x.id.equals(id))
                .findFirst();
    }

    @Override
    public List<Integer> getRemarkableFilmsIdsList() {
        if (remarkableFilmsIds == null) {
            // получаем из SharedPreferences
            SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
            Set<String> remarkableFilmsIdsString = sharedPreferences.getStringSet(SHARED_REMARKABLE_FILMS_IDS, new HashSet<>());
            remarkableFilmsIds = remarkableFilmsIdsString.stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        return remarkableFilmsIds;
    }

    @Override
    public void setRemarkableFilmsIds(List<Integer> remarkableFilmsIds) {
        // сохраняем в SharedPreferences
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> remarkableFilmsIdsString = remarkableFilmsIds
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
        editor.putStringSet(SHARED_REMARKABLE_FILMS_IDS, remarkableFilmsIdsString);
        editor.apply();
        this.remarkableFilmsIds = remarkableFilmsIds;
    }

    @Override
    public List<Movie> getMovieList() {
        return movies;
    }

    @Override
    public List<Movie> getRemarkableMovies() {
        return remarkableMovies;
    }

    @Override
    public void setRemarkableMovies(List<Movie> remarkableMovies) {
        this.remarkableMovies = remarkableMovies;
    }

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
