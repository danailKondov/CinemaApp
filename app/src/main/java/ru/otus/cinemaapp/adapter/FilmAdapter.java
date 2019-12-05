package ru.otus.cinemaapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.fragments.FilmListFragment;
import ru.otus.cinemaapp.model.Movie;

public class FilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private List<Movie> filmList;
    private int checkedPosition;
    private FilmListFragment fragment;
    private List<Integer> remarkableFilmsIdsList;

    public FilmAdapter(FilmListFragment fragment, List<Movie> filmList, List<Integer> remarkableFilmsIdsList) {
        this.fragment = fragment;
        this.filmList = filmList;
        this.remarkableFilmsIdsList = remarkableFilmsIdsList;
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
        Movie movie = filmList.get(position);
        FilmHolder filmHolder = (FilmHolder) holder;
        filmHolder.title.setText(movie.title);

        Glide.with(holder.itemView.getContext())
                .load(POSTER_BASE_URL + movie.posterPath)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_block_black_24dp)
                .into(filmHolder.cover);

        int drawableId = 0;
        if (remarkableFilmsIdsList.contains(movie.id)) {
            drawableId = R.drawable.ic_star_border_red_24dp;
        } else {
            drawableId = R.drawable.ic_star_border_true_black_24dp;
        }
        filmHolder.star.setImageDrawable(ContextCompat.getDrawable(fragment.getContext(), drawableId));


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

    public void setRemarkableFilmsIdsList(List<Integer> remarkableFilmsIdsList) {
        this.remarkableFilmsIdsList = remarkableFilmsIdsList;
    }

    public void setMovies(List<Movie> movies) {
        filmList = movies;
    }

    public class FilmHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_view) TextView title;
        @BindView(R.id.film_image) ImageView cover;
        @BindView(R.id.remarkable_film_star) ImageView star;
        @BindView(R.id.details_button) Button button;

        public FilmHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                fragment.filmItemLongClicked(position);
                return true;
            });

            button.setOnClickListener(view -> {
                int position = getAdapterPosition();
                fragment.detailsButtonClicked(position);
            });
        }
    }
}
