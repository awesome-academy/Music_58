package com.example.music_58.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_58.R;
import com.example.music_58.data.model.Genre;
import com.example.music_58.data.model.GenreKey;
import com.example.music_58.data.model.GenreName;
import com.example.music_58.ui.adapter.GenreAdapter;
import com.example.music_58.ui.genre_detail.GenreDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements GenreAdapter.GenreClickListener {
    private static final int SPAN_COUNT = 2;
    private RecyclerView mRecyclerGenres;
    private List<Genre> mGenres;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(rootView);
        initData();
        return rootView;
    }

    @Override
    public void onItemClick(Genre genre) {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.frame_container, GenreDetailFragment.getInstance(genre))
                .addToBackStack(null)
                .commit();
    }

    private void initData() {
        mGenres.add(new Genre(GenreKey.ALL,
                GenreName.ALL, R.drawable.genre_all));
        mGenres.add(new Genre(GenreKey.AUDIO,
                GenreName.AUDIO, R.drawable.genre_audio));
        mGenres.add(new Genre(GenreKey.ALTERNATIVE,
                GenreName.ALTERNATIVE, R.drawable.genre_alternative_rock));
        mGenres.add(new Genre(GenreKey.AMBIENT,
                GenreName.AMBIENT, R.drawable.genre_ambient));
        mGenres.add(new Genre(GenreKey.CLASSICAL,
                GenreName.CLASSICAL, R.drawable.genre_classical));
        mGenres.add(new Genre(GenreKey.COUNTRY,
                GenreName.COUNTRY, R.drawable.genre_country));
    }

    private void initUI(View rootView) {
        mGenres = new ArrayList<>();
        mRecyclerGenres = rootView.findViewById(R.id.recycler_genres);
        GenreAdapter adapter = new GenreAdapter(getContext(), this, mGenres);
        mRecyclerGenres.setAdapter(adapter);
        mRecyclerGenres.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                SPAN_COUNT, GridLayoutManager.VERTICAL,
                false);
        mRecyclerGenres.setLayoutManager(gridLayoutManager);
    }
}
