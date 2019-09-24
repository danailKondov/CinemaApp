package ru.otus.cinemaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.model.Film;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

import android.os.Bundle;
import android.widget.EditText;

public class AddFilmActivity extends AppCompatActivity {

    @BindView(R.id.titleInput)
    EditText title;

    @BindView(R.id.descriptionInput)
    EditText description;

    private FilmRepositoryInt repository = FilmRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.saveFilmButton)
    void onSaveFilmButtonClick() {
        repository.saveFilm(new Film(
                Film.generateId(),
                title.getText().toString(),
                description.getText().toString(),
                R.drawable.matrix)
        );
        finish();
    }
}
