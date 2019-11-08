package ru.otus.cinemaapp.repo;

import java.util.List;
import java.util.Optional;

import ru.otus.cinemaapp.model.Film;

public interface FilmRepositoryInt {

    Optional<Film> getFilmById(Long id);

    List<Film> getFilmList();

    void saveFilm(Film film);

    List<Film> getRemarkableFilmsList();
}
