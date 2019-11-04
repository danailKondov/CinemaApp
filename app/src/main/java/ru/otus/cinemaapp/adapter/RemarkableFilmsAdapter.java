package ru.otus.cinemaapp.adapter;

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
import ru.otus.cinemaapp.fragments.RemarkableFilmsListFragment;
import ru.otus.cinemaapp.model.Film;

public class RemarkableFilmsAdapter extends RecyclerView.Adapter {

    private List<Film> remarkableFilmsList;
    private RemarkableFilmsListFragment fragment;

    public RemarkableFilmsAdapter(RemarkableFilmsListFragment remarkableFilmsListFragment, List<Film> remarkableFilmsList) {
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
        Film film = remarkableFilmsList.get(position);
        FilmHolder filmHolder = (FilmHolder) holder;
        filmHolder.title.setText(film.getTitle());
        filmHolder.cover.setImageDrawable(ContextCompat.getDrawable(fragment.getContext(), film.getImageResourceId()));
    }

    @Override
    public int getItemCount() {
        return remarkableFilmsList.size();
    }

    public void setRemarkableFilmsList(List<Film> remarkableFilmsList) {
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
