package com.example.music_58.ui.genre_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_58.R;

public class GenreDetailFragment extends Fragment {

    public GenreDetailFragment() {

    }

    public static GenreDetailFragment getInstance() {
        return new GenreDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_genre_detail, container, false);
    }
}
