package com.example.music_58.ui.genre_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_58.R;
import com.example.music_58.data.model.Genre;
import com.example.music_58.data.model.Track;
import com.example.music_58.data.repository.TrackRepository;
import com.example.music_58.data.source.local.TrackLocalDataSource;
import com.example.music_58.data.source.remote.TrackRemoteDataSource;
import com.example.music_58.ui.adapter.TrackAdapter;
import com.example.music_58.util.Constants;
import com.example.music_58.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GenreDetailFragment extends Fragment implements GenreDetailContract.View {
    private TrackAdapter mAdapter;
    private RecyclerView mRecyclerTopSongs;
    private TextView mTextGenreName;
    private TextView mTextRandomPlay;
    private ImageView mGenreImage;
    private List<Track> mTracks;
    private int mOffset;
    private String mGenreKey;
    private String mGenreApi;
    private GenreDetailContract.Presenter mPresenter;

    public GenreDetailFragment() {

    }

    public static GenreDetailFragment getInstance(Genre genre) {
        GenreDetailFragment fragment = new GenreDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_GENRES, genre);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onLoadTracksSuccess(List<Track> tracks) {
        if (mOffset == 0) {
            mAdapter.updateTracks(tracks);
        } else {
            mAdapter.addTracks(tracks);
        }
    }

    @Override
    public void onLoadTracksFail(String message) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genre_detail, container, false);
        initUI(rootView);
        initData();
        return rootView;
    }

    private void initPresenter() {
        TrackRepository repository = TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(getContext())
        );
        mPresenter = new GenreDetailPresenter(repository, this);
    }

    private void initUI(View rootView) {
        mTracks = new ArrayList<>();
        initPresenter();
        mRecyclerTopSongs = rootView.findViewById(R.id.recycler_top_songs);
        mAdapter = new TrackAdapter(getContext(), mTracks);
        mRecyclerTopSongs.setAdapter(mAdapter);
        mRecyclerTopSongs.setHasFixedSize(true);
        mRecyclerTopSongs.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mTextGenreName = rootView.findViewById(R.id.text_music_type);
        mTextRandomPlay = rootView.findViewById(R.id.text_random_play);
        mGenreImage = rootView.findViewById(R.id.image_music_type);
    }

    private void initData() {
        mOffset = 0;
        Bundle bundle = getArguments();
        Genre genre = bundle.getParcelable(Constants.EXTRA_GENRES);
        mGenreKey = genre.getKey();
        mGenreApi = StringUtils.initGenreApi(mGenreKey, mOffset);
        mPresenter.getTracks(mGenreApi);
        mTextGenreName.setText(genre.getName());
        Glide.with(getContext()).load(genre.getPhoto()).centerCrop().into(mGenreImage);
    }
}
