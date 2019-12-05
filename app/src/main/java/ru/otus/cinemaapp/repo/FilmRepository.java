package ru.otus.cinemaapp.repo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import ru.otus.cinemaapp.model.Movie;
import ru.otus.cinemaapp.service.DbService;

public class FilmRepository implements FilmRepositoryInt {

    private static final String SHARED_REMARKABLE_FILMS_IDS = "sharedRemarkableFilmsIds";
    private static final String FIRST_START_DATE = "first_start_date";

    private List<Integer> remarkableFilmsIds;
    private List<Movie> movies = new ArrayList<>();
    private List<Movie> remarkableMovies = Collections.synchronizedList(new ArrayList<>());
    private Activity activity;
    private DataBase dataBase;
    private SharedPreferences sharedPreferences;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
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
    public void addMovies(List<Movie> movies) {
        executorService.execute(() -> dataBase.getMovieDao().saveAll(movies));
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
            Set<String> remarkableFilmsIdsString = sharedPreferences.getStringSet(SHARED_REMARKABLE_FILMS_IDS, new HashSet<>());
            remarkableFilmsIds = remarkableFilmsIdsString.stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        return remarkableFilmsIds;
    }

    @Override
    public void setRemarkableFilmsIds(List<Integer> remarkableFilmsIds) {
        // сохраняем в SharedPreferences
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
    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
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

    @Override
    public void initDB() {
        Objects.requireNonNull(activity);
        dataBase = Room.databaseBuilder(activity, DataBase.class, "movies").build();
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public LiveData<List<Movie>> getMoviesFromDB() {
        return dataBase.getMovieDao().getMovies();
    }

    @Override
    public void setFirstStartDate(LocalDate now) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String date = DateTimeFormatter.ISO_LOCAL_DATE.format(now);
        editor.putString(FIRST_START_DATE, date);
        editor.apply();
    }

    @Override
    public LocalDate getFirstStartDate() {
        String firstStartDate = sharedPreferences.getString(FIRST_START_DATE, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.MIN));
        return LocalDate.parse(firstStartDate);
    }

    @Override
    public void updateAllMovies() {
        Objects.requireNonNull(activity);
        Objects.requireNonNull(dataBase);
        DbService.startUpdateMovies(activity, dataBase);
    }
}
