package ru.otus.cinemaapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.otus.cinemaapp.App;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.adapter.RemarkableFilmsAdapter;
import ru.otus.cinemaapp.model.Movie;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;


public class RemarkableFilmsListFragment extends Fragment {

    public static final String TAG = "RemarkableFilmsListFragment";

    private FilmRepositoryInt repository = FilmRepository.getInstance();
    private FilmListFragment.OnDetailsButtonClickListener detailsButtonClickListener;
    private RemarkableFilmsAdapter adapter;

    @BindView(R.id.remarkableFilmsRecyclerView) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remarkable_films_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new RemarkableFilmsAdapter(this, repository.getRemarkableMovies());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        removeNotActualRemarkableFilms();
        getRemarkableFilmsListFromServer();
    }

    /**
     * Метод проверяет каких фильмов не хватает в Избранном, если их нет в уже загруженных
     * фильмах основного списка, то подгружает их с сервера.
     */
    private void getRemarkableFilmsListFromServer() {

        List<Movie> remarkableMovies = repository.getRemarkableMovies();

        // проверяем каких фильмов не хватает в Избранном
        List<Integer> existingRemarkableMoviesIds = remarkableMovies
                .stream()
                .map(movie -> movie.id)
                .collect(Collectors.toList());

        List<Integer> remarkableMoviesIdsToDownload = new ArrayList<>(repository.getRemarkableFilmsIdsList());
        remarkableMoviesIdsToDownload.removeAll(existingRemarkableMoviesIds);

        for (Integer id : remarkableMoviesIdsToDownload) {

            // потом проверяем уже загруженные фильмы
            Optional<Movie> optionalMovie = repository.getMovies()
                    .stream()
                    .filter(movie -> id.equals(movie.id))
                    .findFirst();
            if(optionalMovie.isPresent()) {
                remarkableMovies.add(optionalMovie.get());
                adapter.setRemarkableFilmsList(remarkableMovies);
                adapter.notifyItemInserted(remarkableMovies.size() - 1);
                continue;
            }

            // затем скачиваем их с сервера
            App.getInstance().service.getMovieById(id).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful()) {
                        Movie movie = response.body();
                        if (movie != null) {
                            remarkableMovies.add(movie);
                            adapter.setRemarkableFilmsList(remarkableMovies);
                            adapter.notifyItemInserted(remarkableMovies.size() - 1);
                            Log.i("Movie download", "added to remarkable films " + movie.title);
                        }
                    } else {
                        Log.e("Movie download", "response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.e("Movie download", t.getMessage());
                }
            });
        }
    }

    /**
     * Метод удаляет из списка Избранных фильмов те, id которых нет в списке id избранных фильмов
     */
    private void removeNotActualRemarkableFilms() {
        List<Movie> remarkableMovies = repository.getRemarkableMovies();
        int initialSize = remarkableMovies.size();
        remarkableMovies = remarkableMovies
                .stream()
                .filter(movie -> repository.getRemarkableFilmsIdsList().contains(movie.id))
                .collect(Collectors.toList());
        int afterSize = remarkableMovies.size();
        if (initialSize != afterSize) {
            repository.setRemarkableMovies(remarkableMovies);
            adapter.setRemarkableFilmsList(remarkableMovies);
            adapter.notifyDataSetChanged();
        }
    }

    public void detailsButtonClicked(int position) {
        detailsButtonClickListener.onDetailsButtonClicked(position, true);
    }

    public void setDetailsButtonClickListener(FilmListFragment.OnDetailsButtonClickListener detailsButtonClickListener) {
        this.detailsButtonClickListener = detailsButtonClickListener;
    }

    public void removeRemarkableClicked(int position) {
        Movie movie = repository.getRemarkableMovies().remove(position);
        List<Integer> remarkableFilmsIdsList = repository.getRemarkableFilmsIdsList();
        remarkableFilmsIdsList.remove(movie.id);
        repository.setRemarkableFilmsIds(remarkableFilmsIdsList);
        adapter.setRemarkableFilmsList(repository.getRemarkableMovies());
        adapter.notifyItemRemoved(position);
    }
}
