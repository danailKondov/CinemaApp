package ru.otus.cinemaapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ru.otus.cinemaapp.R;

public class AboutInfoFragment extends Fragment {

    public static final String TAG = "AboutInfoFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
