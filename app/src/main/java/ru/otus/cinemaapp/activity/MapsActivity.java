package ru.otus.cinemaapp.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;
import ru.otus.cinemaapp.BuildConfig;
import ru.otus.cinemaapp.R;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PlacesListener {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = "MapsActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private final LatLng defaultLocation = new LatLng(55.753960, 37.620393); // Москва

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        getLocationPermission();
        if (locationPermissionGranted) {
            showCinemaOnMap();
        }
    }

    private void showCinemaOnMap() {
        Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                if (location != null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), DEFAULT_ZOOM));

                    new NRPlaces.Builder()
                            .listener(MapsActivity.this)
                            .key(BuildConfig.GOOGLE_MAPS_KEY)
                            .latlng(location.getLatitude(), location.getLongitude())
                            .radius(1500)
                            .type(PlaceType.MOVIE_THEATER)
                            .build()
                            .execute();
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    map.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                }
            } else {
                Log.e(TAG, "Exception: %s", task.getException());
                map.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    showCinemaOnMap();
                }
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onPlacesFailure(PlacesException e) {
        Log.i("PlacesAPI", "onPlacesFailure()");
    }

    @Override
    public void onPlacesStart() {
        Log.i("PlacesAPI", "onPlacesStart()");
    }

    @Override
    public void onPlacesSuccess(List<noman.googleplaces.Place> places) {
        Log.i("PlacesAPI", "onPlacesSuccess()");
        runOnUiThread(() -> {
            for (noman.googleplaces.Place place : places) {
                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                map.addMarker(new MarkerOptions().position(latLng)
                        .title(place.getName()).snippet(place.getVicinity()));
            }
        });
    }

    @Override
    public void onPlacesFinished() {
        Log.i("PlacesAPI", "onPlacesFinished()");
    }
}
