package ru.otus.cinemaapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.model.Film;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSaveCommentButtonListener} interface
 * to handle interaction events.
 * Use the {@link FilmDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilmDetailsFragment extends Fragment {

    public static final String TAG = "filmDetailsFragment";
    private static final String FILM_ID = "filmId";
    private static final String POSITION = "position";

    private long filmId;
    private int position;

    @BindView(R.id.likeCheckBox) CheckBox likeCheckBox;
    @BindView(R.id.commentTextInput) EditText comment;
    @BindView(R.id.film_image_details) ImageView cover;

    private FilmRepositoryInt repository = FilmRepository.getInstance();

    private OnSaveCommentButtonListener saveCommentButtonListener;

    public FilmDetailsFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filmId film id
     * @param position adapter position
     * @return A new instance of fragment FilmDetailsFragment.
     */
    public static FilmDetailsFragment newInstance(Long filmId, int position) {
        FilmDetailsFragment fragment = new FilmDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(FILM_ID, filmId);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filmId = getArguments().getLong(FILM_ID);
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Film film = repository.getFilmById(filmId)
                .orElseGet(() -> new Film(-1L, "title", "No such film", 0));
        likeCheckBox.setChecked(film.isLiked());
        cover.setImageDrawable(ContextCompat.getDrawable(getContext(), film.getImageResourceId()));

        Button button = view.findViewById(R.id.saveCommentButton);
        button.setOnClickListener(v -> {
            film.setLiked(likeCheckBox.isChecked());
            onSaveCommentButtonPressed(likeCheckBox.isChecked(), comment.getText().toString(), filmId, position);
        });
    }

    public void onSaveCommentButtonPressed(boolean isChecked, String commentText, long filmId, int position) {
        if (saveCommentButtonListener != null) {
            saveCommentButtonListener.onSaveCommentButtonPressed(isChecked, commentText, filmId, position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveCommentButtonListener) {
            saveCommentButtonListener = (OnSaveCommentButtonListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSaveCommentButtonListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveCommentButtonListener = null;
    }

    public interface OnSaveCommentButtonListener {
        void onSaveCommentButtonPressed(boolean isChecked, String commentText, long filmId, int position);
    }
}
