package ru.otus.cinemaapp.presenters;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.Observer;
import ru.otus.cinemaapp.fragments.FilmListFragmentInt;
import ru.otus.cinemaapp.model.Movie;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

public class FilmListFragmentPresenter implements Observer<List<Movie>> {

    private WeakReference<FilmListFragmentInt> fragmentWeakRef;
    private FilmRepositoryInt repository;

    public FilmListFragmentPresenter(FilmRepositoryInt repository) {
        this.repository = repository;
    }

    public void registerFragment(FilmListFragmentInt filmListFragment) {
        fragmentWeakRef = new WeakReference<>(filmListFragment);
    }

    private void registerThisAsMoviesFromDbObserver() {
        getFragment().addMovieListObserver(repository.getMoviesFromDB());
    }

    private FilmListFragmentInt getFragment() {
        return fragmentWeakRef.get();
    }

    @Override
    public void onChanged(List<Movie> movies) {
        repository.setMovies(movies);
        getFragment().showMoviesList(movies);
    }

    public List<Integer> getRemarkableFilmsIdsList() {
        return repository.getRemarkableFilmsIdsList();
    }

    public void onViewCreated(FilmListFragmentInt filmListFragment) {
        registerFragment(filmListFragment);
        registerThisAsMoviesFromDbObserver();
        checkIsFirstStartToday();
    }

    private void checkIsFirstStartToday() {
        if (isFirstStartToday()) {
            repository.updateAllMovies();
            repository.setFirstStartDate(LocalDate.now());
        }
    }

    private boolean isFirstStartToday() {
        LocalDate localDateNow = LocalDate.now();
        LocalDate firstStartDate = repository.getFirstStartDate();
        return !firstStartDate.isEqual(localDateNow);
    }

    public void onFilmItemLongClicked(int position) {
        Movie movie = repository.getMovies().get(position);
        List<Integer> remarkableFilmsIdsList = repository.getRemarkableFilmsIdsList();
        boolean isRemarkable = false;
        if (remarkableFilmsIdsList.contains(movie.id)) {
            remarkableFilmsIdsList.remove(movie.id);
        } else {
            remarkableFilmsIdsList.add(movie.id);
            isRemarkable = true;
        }
        repository.setRemarkableFilmsIds(remarkableFilmsIdsList);
        getFragment().showRemarkableFilmsList(remarkableFilmsIdsList, isRemarkable);
    }
}
