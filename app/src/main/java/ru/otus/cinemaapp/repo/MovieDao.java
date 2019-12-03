package ru.otus.cinemaapp.repo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import ru.otus.cinemaapp.model.Movie;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addMovie(Movie movie);

    @Query("SELECT * FROM MOVIES")
    LiveData<List<Movie>> getMovies();


    @Query("SELECT * FROM MOVIES WHERE id = :id")
    Movie getMovieById(String id);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(Iterable<Movie> movies);

    @Delete
    int deleteMovie(Movie movie);

    @Transaction
    @Query("DELETE FROM MOVIES")
    int deleteAll();
}
