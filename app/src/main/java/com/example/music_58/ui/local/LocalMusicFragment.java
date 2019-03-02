package com.example.music_58.ui.local;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_58.R;
import com.example.music_58.ui.favorite.FavoriteFragment;

public class LocalMusicFragment extends Fragment {

    public LocalMusicFragment() {

    }

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }
}
