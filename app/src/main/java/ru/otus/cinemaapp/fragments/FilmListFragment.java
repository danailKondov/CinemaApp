package ru.otus.cinemaapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.adapter.FilmAdapter;
import ru.otus.cinemaapp.model.Movie;
import ru.otus.cinemaapp.presenters.FilmListFragmentPresenter;
import ru.otus.cinemaapp.repo.FilmRepository;


public class FilmListFragment extends Fragment implements FilmListFragmentInt {

    public static final String CHECKED_POSITION = "checkedPosition";
    public static final String TAG = "FilmListFragment";

    private FilmListFragmentPresenter presenter = new FilmListFragmentPresenter(FilmRepository.getInstance());

    private FilmAdapter adapter;
    private int checkedPosition = -1;
    private OnDetailsButtonClickListener detailsButtonClickListener;
    private OnInviteFriendClickListener inviteFriendClickListener;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;


    public FilmListFragment() {}

    public static FilmListFragment newInstance(int position) {
        FilmListFragment fragment = new FilmListFragment();
        Bundle args = new Bundle();
        args.putInt(CHECKED_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_film_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void inviteFriend() {
        inviteFriendClickListener.onInviteFriendClicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            checkedPosition = getArguments().getInt(CHECKED_POSITION);
        }
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            adapter = new FilmAdapter(this, new ArrayList<>(), presenter.getRemarkableFilmsIdsList());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            presenter.onViewCreated(this);

            FloatingActionButton actionButton = view.findViewById(R.id.contactFriendFab);
            actionButton.setOnClickListener(v -> inviteFriend());

            if (savedInstanceState != null) {
                checkedPosition = savedInstanceState.getInt(CHECKED_POSITION);
            }
            changeDetailButtonTextColor();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CHECKED_POSITION, checkedPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * Метод обрабатывает нажатие на кнопку "Далее".
     * @param position позиция фильма
     */
    public void detailsButtonClicked(int position) {
        checkedPosition = position;
        changeDetailButtonTextColor();
        detailsButtonClickListener.onDetailsButtonClicked(position, false);
    }

    public void setDetailsButtonClickListener(OnDetailsButtonClickListener detailsButtonClickListener) {
        this.detailsButtonClickListener = detailsButtonClickListener;
    }

    public void setInviteFriendClickListener(OnInviteFriendClickListener inviteFriendClickListener) {
        this.inviteFriendClickListener = inviteFriendClickListener;
    }

    /**
     * Метод  отмечает синим цветом текст кнопки "Далее", выбранной пользователем,
     * цвет остальных кнопок - черный.
     */
    private void changeDetailButtonTextColor() {
        adapter.setCheckedPosition(checkedPosition);
        adapter.notifyDataSetChanged();
    }

    /**
     * Метод по долгому нажатию добавляет id фильма в список избранных если его там нет
     * и удаляет, если есть.
     * @param position позиция фильма
     */
    @Override
    public void addFilmToRemarkableList(int position) {
        presenter.onFilmItemLongClicked(position);
    }

    @Override
    public void addMovieListObserver(LiveData<List<Movie>> moviesFromDB) {
        moviesFromDB.observe(getActivity(), presenter);
    }

    @Override
    public void showMoviesList(List<Movie> movies) {
        adapter.setMovies(movies);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showRemarkableFilmsList(List<Integer> remarkableFilmsIdsList, boolean isRemarkable) {
        adapter.setRemarkableFilmsIdsList(remarkableFilmsIdsList);
        adapter.notifyDataSetChanged();
        Snackbar
                .make(
                        getView(),
                        isRemarkable ? getString(R.string.film_set_remarkable) : getString(R.string.film_set_not_remarkable),
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public interface OnDetailsButtonClickListener {
        void onDetailsButtonClicked(int position, boolean isRemarkable);
    }

    public interface OnInviteFriendClickListener {
        void onInviteFriendClicked();
    }

}
