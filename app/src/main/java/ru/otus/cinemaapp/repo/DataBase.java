package ru.otus.cinemaapp.repo;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import ru.otus.cinemaapp.model.Movie;


@Database(entities = {
        Movie.class},
        version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    public abstract MovieDao getMovieDao();
}
