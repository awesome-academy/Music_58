package com.example.music_58.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_58.R;
import com.example.music_58.ui.adapter.GenreAdapter;

public class HomeFragment extends Fragment implements GenreAdapter.GenreClickListener {
    private RecyclerView mRecyclerGenres;
    private static final int SPAN_COUNT = 2;

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
        return rootView;
    }

    private void initUI(View rootView) {
        mRecyclerGenres = rootView.findViewById(R.id.recycler_genres);
        GenreAdapter adapter = new GenreAdapter(getContext(), this);
        mRecyclerGenres.setAdapter(adapter);
        mRecyclerGenres.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                SPAN_COUNT, GridLayoutManager.VERTICAL,
                false);
        mRecyclerGenres.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onItemClick(int position) {

    }
}
