package ru.otus.cinemaapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.adapter.FilmAdapter;
import ru.otus.cinemaapp.model.Film;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

public class MainActivity extends AppCompatActivity {

    public static final String FILM_ID = "filmId";
    public static final String CHECKED_POSITION = "checkedPosition";
    public static final String FRIEND_INVITE = "Приглашение посмотреть кино";
    public static final int REQUEST_FOR_COMMENT = 1;

    private FilmRepositoryInt repository = FilmRepository.getInstance();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int checkedPosition = -1;

    private FilmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new FilmAdapter(this, repository.getFilmList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState != null) {
            checkedPosition = savedInstanceState.getInt(CHECKED_POSITION);
        }
        changeDetailButtonTextColor();

        FloatingActionButton actionButton = findViewById(R.id.contactFriendFab);
        actionButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, FRIEND_INVITE);
            intent.setType("text/plain");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }

    /**
     * Метод обрабатывает нажатие на кнопку "Далее".
     * @param position позиция фильма
     */
    public void detailsButtonClicked(int position) {
        checkedPosition = position;
        changeDetailButtonTextColor();

        Film film = repository.getFilmList().get(position);
        Intent intent = new Intent(this, FilmDetailsActivity.class);
        intent.putExtra(FILM_ID, film.getId());
        startActivityForResult(intent, REQUEST_FOR_COMMENT);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHECKED_POSITION, checkedPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_COMMENT && data != null) {
            boolean isLiked = false;
            String comment = null;
            if (resultCode == RESULT_OK) {
                isLiked = data.getBooleanExtra(FilmDetailsActivity.LIKE, false);
                comment = data.getStringExtra(FilmDetailsActivity.COMMENT);
            }
            Log.i("activityResult", "liked: " + isLiked + "; comment: " + comment);
        }
    }

    /**
     * Метод  отмечает синим цветом текст кнопки "Далее", выбранной пользователем,
     * цвет остальных кнопок - черный.
     */
    private void changeDetailButtonTextColor() {
        adapter.setCheckedPosition(checkedPosition);
        adapter.notifyDataSetChanged();
    }
}
