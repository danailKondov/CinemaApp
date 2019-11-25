package ru.otus.cinemaapp;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.otus.cinemaapp.service.MovieApiService;

public class App extends Application {

    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static App instance;

    public MovieApiService service;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initRetrofit();
    }

    private void initRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        String token = "Bearer " + BuildConfig.MOVIE_DB_KEY;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder().addHeader("Authorization", token).build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MovieApiService.class);
    }

    public static App getInstance() {
        return instance;
    }
}
