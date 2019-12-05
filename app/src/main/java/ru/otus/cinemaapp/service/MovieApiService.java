package ru.otus.cinemaapp.service;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.otus.cinemaapp.model.Movie;
import ru.otus.cinemaapp.model.PopularMoviesQueryResult;

public interface MovieApiService {

    @GET("3/movie/popular")
    Observable<PopularMoviesQueryResult> getMovies();

    @GET("3/movie/{movie_id}")
    Call<Movie> getMovieById(@Path("movie_id") Integer movieId);
}
