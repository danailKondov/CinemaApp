package ru.otus.cinemaapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.adapter.RemarkableFilmsAdapter;
import ru.otus.cinemaapp.model.Film;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

/**
 * A simple {@link Fragment} subclass.
 */
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

        adapter = new RemarkableFilmsAdapter(this, repository.getRemarkableFilmsList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void detailsButtonClicked(int position) {
        detailsButtonClickListener.onDetailsButtonClicked(position, true);
    }

    public FilmListFragment.OnDetailsButtonClickListener getDetailsButtonClickListener() {
        return detailsButtonClickListener;
    }

    public void setDetailsButtonClickListener(FilmListFragment.OnDetailsButtonClickListener detailsButtonClickListener) {
        this.detailsButtonClickListener = detailsButtonClickListener;
    }

    public void removeRemarkableClicked(int position) {
        Film film = repository.getRemarkableFilmsList().get(position);
        film.setRemarkable(false);
        adapter.setRemarkableFilmsList(repository.getRemarkableFilmsList());
        adapter.notifyItemRemoved(position);
    }
}
