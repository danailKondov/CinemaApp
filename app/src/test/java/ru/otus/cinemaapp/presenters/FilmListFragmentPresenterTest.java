package ru.otus.cinemaapp.presenters;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.otus.cinemaapp.fragments.FilmListFragmentInt;
import ru.otus.cinemaapp.model.Movie;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class FilmListFragmentPresenterTest {

    private static final int POSITION = 0;
    public static final int FIRST_ID = 1;
    public static final int SECOND_ID = 2;
    private FilmRepositoryInt repository;
    private FilmListFragmentPresenter presenter;
    private FilmListFragmentInt fragment;
    private InOrder orderVerifier;

    @Before
    public void init() {
        repository = mock(FilmRepositoryInt.class);
        presenter = new FilmListFragmentPresenter(repository);
        fragment = mock(FilmListFragmentInt.class);
        presenter.registerFragment(fragment);
        orderVerifier = inOrder(fragment, repository);
    }

    @Test
    public void movieListChangedTest() {
        List<Movie> movies = getTestMovies();

        presenter.onChanged(movies);

        orderVerifier.verify(repository).setMovies(movies);
        orderVerifier.verify(fragment).showMoviesList(movies);

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(fragment);
    }

    @Test
    public void testCheckIsFirstStartTodayWhenIsFirst() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        when(repository.getFirstStartDate()).thenReturn(yesterday);

        presenter.onViewCreated(fragment);

        orderVerifier.verify(repository).getFirstStartDate();
        orderVerifier.verify(repository).updateAllMovies();
        orderVerifier.verify(repository).setFirstStartDate(LocalDate.now());
    }

    @Test
    public void testCheckIsFirstStartTodayWhenIsNotFirst() {
        LocalDate today = LocalDate.now();
        when(repository.getFirstStartDate()).thenReturn(today);

        presenter.onViewCreated(fragment);

        orderVerifier.verify(repository).getFirstStartDate();
    }

    @Test
    public void testFilmItemLongClickedWhenRemovingRemarkableFilmsFromList() {
        List<Movie> movies = getTestMovies();
        when(repository.getMovies()).thenReturn(movies);
        when(repository.getRemarkableFilmsIdsList()).thenReturn(new ArrayList<>(Arrays.asList(FIRST_ID)));

        presenter.onFilmItemLongClicked(POSITION);

        verify(fragment).showRemarkableFilmsList(Collections.emptyList(), false);
    }

    @Test
    public void testFilmItemLongClickedWhenAddingRemarkableFilmsToList() {
        List<Movie> movies = getTestMovies();
        when(repository.getMovies()).thenReturn(movies);
        when(repository.getRemarkableFilmsIdsList()).thenReturn(new ArrayList<>(Arrays.asList(SECOND_ID)));

        presenter.onFilmItemLongClicked(POSITION);

        verify(fragment).showRemarkableFilmsList(new ArrayList<>(Arrays.asList(SECOND_ID, FIRST_ID)), true);
    }

    private List<Movie> getTestMovies() {
        Movie firstMovie = new Movie(FIRST_ID, "overview", "title", true);
        Movie secondMovie = new Movie(SECOND_ID, "overview2", "title2", false);
        return Arrays.asList(firstMovie, secondMovie);
    }
}