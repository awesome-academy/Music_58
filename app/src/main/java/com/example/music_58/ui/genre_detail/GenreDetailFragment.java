package com.example.music_58.ui.genre_detail;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_58.R;
import com.example.music_58.data.model.Genre;
import com.example.music_58.data.model.Track;
import com.example.music_58.data.repository.TrackRepository;
import com.example.music_58.data.source.local.TrackLocalDataSource;
import com.example.music_58.data.source.remote.TrackRemoteDataSource;
import com.example.music_58.service.MediaPlayerService;
import com.example.music_58.ui.BaseLoadMoreFragment;
import com.example.music_58.ui.adapter.TrackAdapter;
import com.example.music_58.ui.main_play.MainPlayActivity;
import com.example.music_58.util.Constants;
import com.example.music_58.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.BIND_AUTO_CREATE;

public class GenreDetailFragment extends BaseLoadMoreFragment implements
        GenreDetailContract.View, SwipeRefreshLayout.OnRefreshListener,
        TrackAdapter.OnTrackClickListener, View.OnClickListener {
    private static final int OFFSET = 20;
    private TrackAdapter mAdapter;
    private RecyclerView mRecyclerTopSongs;
    private TextView mTextGenreName;
    private TextView mTextRandomPlay;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private ImageView mGenreImage;
    private List<Track> mTracks;
    private int mOffset;
    private String mGenreKey;
    private String mGenreApi;
    private GenreDetailContract.Presenter mPresenter;
    private View mView;
    private MediaPlayerService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) iBinder;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            getContext().unbindService(mConnection);
        }
    };

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
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.addTracks(tracks);
            mProgressBar.setVisibility(View.GONE);
        }
        mTextRandomPlay.setEnabled(true);
        Intent serviceIntent = MediaPlayerService.getPlayMusicServiceIntent(getContext());
        if (mService == null) {
            getActivity().startService(serviceIntent);
        }
        getActivity().bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onLoadTracksFail(String message) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (mGenreKey != null && !mGenreKey.isEmpty()) {
            mOffset = 0;
            String api = StringUtils.initGenreApi(mGenreKey, mOffset);
            mPresenter.getTracks(api);
        }
    }

    @Override
    public void loadMoreData() {
        mIsScrolling = false;
        mProgressBar.setVisibility(View.VISIBLE);
        mOffset += OFFSET;
        String api = StringUtils.initGenreApi(mGenreKey, mOffset);
        mPresenter.getTracks(api);
    }

    @Override
    public void initViewLoadMore() {
        mRecyclerView = mView.findViewById(R.id.recycler_top_songs);
        mTracks = new ArrayList<>();
        mAdapter = new TrackAdapter(getContext(), mTracks, this);
        mRecyclerView.setAdapter(mAdapter);
        mLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mProgressBar = mView.findViewById(R.id.proress_load_more);
        mProgressBar.setVisibility(View.GONE);
        setLoadMore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genre_detail,
                container, false);
        mView = rootView;
        initUI(rootView);
        initViewLoadMore();
        initData();
        return rootView;
    }

    @Override
    public void onTrackClick(int position) {
        mService.setTracks(mTracks);
        mService.requestCreate(position);
        startActivity(MainPlayActivity.getIntent(getContext()));
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        int position = random.nextInt(mTracks.size());
        mService.setTracks(mTracks);
        mService.requestCreate(position);
        startActivity(MainPlayActivity.getIntent(getContext()));
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
        mAdapter = new TrackAdapter(getContext(), mTracks, this);
        mRecyclerTopSongs.setAdapter(mAdapter);
        mRecyclerTopSongs.setHasFixedSize(true);
        mRecyclerTopSongs.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mTextGenreName = rootView.findViewById(R.id.text_music_type);
        mTextRandomPlay = rootView.findViewById(R.id.text_random_play);
        mTextRandomPlay.setOnClickListener(this);
        mGenreImage = rootView.findViewById(R.id.image_music_type);
        mProgressBar = rootView.findViewById(R.id.proress_load_more);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mToolbar = rootView.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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
