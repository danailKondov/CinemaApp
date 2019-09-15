package ru.otus.cinemaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.model.Film;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FilmDetailsActivity extends AppCompatActivity {

    public static final String LIKE = "like";
    public static final String COMMENT = "comment";

    private FilmRepositoryInt repository = FilmRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        Intent intent = getIntent();
        Long id = intent.getLongExtra(MainActivity.FILM_ID, -1L);
        Film film = repository.getFilmById(id)
                .orElseGet(() -> new Film(-1L, "title", "No such film", 0));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(film.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ((TextView)findViewById(R.id.title_details)).setText(film.getTitle());
        ((TextView)findViewById(R.id.film_description)).setText(film.getDescription());
        ((ImageView)findViewById(R.id.film_image_details))
                .setImageDrawable(ContextCompat.getDrawable(this, film.getImageResourceId()));

        Button button = findViewById(R.id.saveCommentButton);
        button.setOnClickListener(view -> {
            Intent responseIntent = new Intent();
            responseIntent.putExtra(LIKE, ((CheckBox)findViewById(R.id.likeCheckBox)).isChecked());
            responseIntent.putExtra(COMMENT, ((EditText)findViewById(R.id.commentTextInput)).getText().toString());
            setResult(RESULT_OK, responseIntent);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
