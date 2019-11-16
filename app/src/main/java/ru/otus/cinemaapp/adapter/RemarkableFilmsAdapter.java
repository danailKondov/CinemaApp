package ru.otus.cinemaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.fragments.RemarkableFilmsListFragment;
import ru.otus.cinemaapp.model.Movie;

public class RemarkableFilmsAdapter extends RecyclerView.Adapter {

    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private List<Movie> remarkableFilmsList;
    private RemarkableFilmsListFragment fragment;

    public RemarkableFilmsAdapter(RemarkableFilmsListFragment remarkableFilmsListFragment, List<Movie> remarkableFilmsList) {
        this.fragment = remarkableFilmsListFragment;
        this.remarkableFilmsList = remarkableFilmsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_remarkable_film, parent, false);
        return new RemarkableFilmsAdapter.FilmHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = remarkableFilmsList.get(position);
        FilmHolder filmHolder = (FilmHolder) holder;
        filmHolder.title.setText(movie.title);

        Glide.with(holder.itemView.getContext())
                .load(POSTER_BASE_URL + movie.posterPath)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_block_black_24dp)
                .into(filmHolder.cover);
    }

    @Override
    public int getItemCount() {
        return remarkableFilmsList.size();
    }

    public void setRemarkableFilmsList(List<Movie> remarkableFilmsList) {
        this.remarkableFilmsList = remarkableFilmsList;
    }

    class FilmHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_view) TextView title;
        @BindView(R.id.film_image) ImageView cover;
        @BindView(R.id.details_button) Button button;
        @BindView(R.id.remove_remarkable_button) Button removeButton;

        public FilmHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            button.setOnClickListener(view -> {
                int position = getAdapterPosition();
                fragment.detailsButtonClicked(position);
            });

            removeButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                fragment.removeRemarkableClicked(position);
            });
        }
    }
}
