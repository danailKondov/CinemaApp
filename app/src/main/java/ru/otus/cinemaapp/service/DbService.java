package ru.otus.cinemaapp.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.otus.cinemaapp.App;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.activity.MainActivity;
import ru.otus.cinemaapp.repo.DataBase;

public class DbService extends IntentService {

    private static final String CHANNEL_ID = "Cinema_channel";
    private static DataBase dataBase;
    private int notificationId;

    public DbService() {
        super("DbService");
    }

    public static void startUpdateMovies(Context context, DataBase db) {
        dataBase = db;
        Intent intent = new Intent(context, DbService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            handleUpdateMovies();
        }
    }

    private void handleUpdateMovies() {
        Observable.fromCallable(() -> dataBase.getMovieDao().deleteAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Integer integer) {}

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DbService", "Error deleting all movies in update operation", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("DbService", "All movies deleted successfully. Starting download from server...");
                        getMoviesFromServer();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getMoviesFromServer() {

        App.getInstance().service.getMovies()
                .subscribeOn(Schedulers.io())
                .map(result -> result.results)
                .doOnNext(movies -> dataBase.getMovieDao().saveAll(movies))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (movies) -> {
                            createNotificationChannel();
                            createPushNotification();
                        },
                        e -> Log.e("DbService", "Error: ", e)
                );
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createPushNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_proector_notification_item)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(++notificationId, builder.build());
    }
}
