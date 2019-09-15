package ru.otus.cinemaapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.activity.MainActivity;
import ru.otus.cinemaapp.model.Film;

public class FilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Film> filmList;
    private int checkedPosition;
    private MainActivity mainActivity;

    public FilmAdapter(MainActivity mainActivity, List<Film> filmList) {
        this.mainActivity = mainActivity;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_film, parent, false);
        return new FilmHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Film film = filmList.get(position);
        FilmHolder filmHolder = (FilmHolder) holder;
        filmHolder.title.setText(film.getTitle());
        filmHolder.cover.setImageDrawable(ContextCompat.getDrawable(mainActivity, film.getImageResourceId()));
        if (position == checkedPosition) {
            filmHolder.button.setTextColor(Color.CYAN);
        } else {
            filmHolder.button.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    public class FilmHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_view) TextView title;
        @BindView(R.id.film_image) ImageView cover;
        @BindView(R.id.details_button) Button button;

        public FilmHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            button.setOnClickListener(view -> {
                int position = getAdapterPosition();
                mainActivity.detailsButtonClicked(position);
            });
        }
    }
}
