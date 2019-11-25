package ru.otus.cinemaapp.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

public class AddFilmFragment extends Fragment {

    public static final String TAG = "add";
    @BindView(R.id.titleInput) EditText title;
    @BindView(R.id.descriptionInput) EditText description;

    private FilmRepositoryInt repository = FilmRepository.getInstance();
    private OnFilmAddedListener listener;

    public AddFilmFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_film, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.saveFilmButton)
    void onSaveFilmButtonClick(Button button) {
        hideKeyboard(button);
        listener.onFilmAdded();
        throw new UnsupportedOperationException();
    }

    private void hideKeyboard(Button button) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(button.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setListener(OnFilmAddedListener listener) {
        this.listener = listener;
    }

    public interface OnFilmAddedListener {
        void onFilmAdded();
    }

}
