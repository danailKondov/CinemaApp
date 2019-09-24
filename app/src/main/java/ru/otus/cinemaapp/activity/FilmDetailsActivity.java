package ru.otus.cinemaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
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
    public static final String VIEW_TITLE = "title";
    public static final String VIEW_COVER = "cover";

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

        TextView title = findViewById(R.id.title_details);
        title.setText(film.getTitle());
        ViewCompat.setTransitionName(title, VIEW_TITLE);

        TextView description = findViewById(R.id.film_description);
        description.setText(film.getDescription());

        ImageView cover = findViewById(R.id.film_image_details);
        cover.setImageDrawable(ContextCompat.getDrawable(this, film.getImageResourceId()));
        ViewCompat.setTransitionName(cover, VIEW_COVER);

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
