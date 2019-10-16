package ru.otus.cinemaapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.otus.cinemaapp.R;
import ru.otus.cinemaapp.fragments.AddFilmFragment;
import ru.otus.cinemaapp.fragments.FilmDetailsFragment;
import ru.otus.cinemaapp.fragments.FilmListFragment;
import ru.otus.cinemaapp.model.Film;
import ru.otus.cinemaapp.repo.FilmRepository;
import ru.otus.cinemaapp.repo.FilmRepositoryInt;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FilmListFragment.OnDetailsButtonClickListener,
        FilmListFragment.OnInviteFriendClickListener,
        FilmDetailsFragment.OnSaveCommentButtonListener,
        AddFilmFragment.OnFilmAddedListener {

    public static final String FRIEND_INVITE = "Приглашение посмотреть кино";

    private FilmRepositoryInt repository = FilmRepository.getInstance();

    @BindView(R.id.main_coordinator_layout) CoordinatorLayout coordLayout;
    @BindView(R.id.navigationView) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        attachFilmListFragment(-1);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void attachFilmListFragment(int position) {
        FilmListFragment filmListFragment = (FilmListFragment) getSupportFragmentManager()
                .findFragmentByTag(FilmListFragment.TAG);
        if (filmListFragment == null) {
            filmListFragment = FilmListFragment.newInstance(position);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, filmListFragment, FilmListFragment.TAG)
                .addToBackStack(FilmListFragment.TAG)
                .commit();
    }

    private void inviteFriend() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, FRIEND_INVITE);
        intent.setType("text/plain");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contactFriendOptionsItem:
                inviteFriend();
                return true;
            case R.id.addFilmOptionsItem:
                attachAddFilmFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attachAddFilmFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new AddFilmFragment(), AddFilmFragment.TAG)
                .addToBackStack(AddFilmFragment.TAG)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case (R.id.drawer_exit):
                showExitDialog();
                return true;
        }
        return true;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof FilmListFragment) {
            FilmListFragment filmListFragment = (FilmListFragment) fragment;
            filmListFragment.setDetailsButtonClickListener(this);
            filmListFragment.setInviteFriendClickListener(this);
        } else if (fragment instanceof AddFilmFragment) {
            AddFilmFragment addFilmFragment = (AddFilmFragment) fragment;
            addFilmFragment.setListener(this);
        }
    }

    private void showExitDialog() {
        new ExitDialog(this).show();
    }

    @Override
    public void onDetailsButtonClicked(int position) {
        Film film = repository.getFilmList().get(position);
        attachFilmDetailsFragment(film.getId(), position);
    }

    private void attachFilmDetailsFragment(Long filmId, int position) {
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.setExpanded(false);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, FilmDetailsFragment.newInstance(filmId, position), FilmDetailsFragment.TAG)
                .addToBackStack(FilmDetailsFragment.TAG)
                .commit();
    }

    @Override
    public void onSaveCommentButtonPressed(boolean isChecked, String commentText, long filmId, int position) {
        getSupportFragmentManager().popBackStack();
        Snackbar
                .make(
                        coordLayout,
                        isChecked ? getString(R.string.add_to_prefs) : getString(R.string.not_add_to_prefs),
                        Snackbar.LENGTH_LONG
                )
                    .setAction(getString(R.string.snackbar_back), view ->
                            getSupportFragmentManager().popBackStack()
                    )
                .setActionTextColor(Color.CYAN)
                .show();
        Log.i("activityResult", "liked: " + isChecked + "; comment: " + commentText);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onInviteFriendClicked() {
        inviteFriend();
    }

    @Override
    public void onFilmAdded() {
        getSupportFragmentManager().popBackStack();
    }

    class ExitDialog extends Dialog {

        public ExitDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog);
            ButterKnife.bind(this);
        }

        @OnClick(R.id.dialog_exit)
        void onExitClick(View view) {
            dismiss();
            MainActivity.this.finish();
        }

        @OnClick(R.id.dialog_remain)
        void onRemainClick(View view) {
            dismiss();
        }
    }
}
