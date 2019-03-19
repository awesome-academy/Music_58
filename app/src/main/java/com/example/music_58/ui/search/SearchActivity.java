package com.example.music_58.ui.search;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.music_58.R;
import com.example.music_58.data.model.Track;
import com.example.music_58.data.repository.TrackRepository;
import com.example.music_58.data.source.local.TrackLocalDataSource;
import com.example.music_58.data.source.remote.TrackRemoteDataSource;
import com.example.music_58.service.MediaPlayerService;
import com.example.music_58.ui.BaseLoadMoreActivity;
import com.example.music_58.ui.adapter.TrackAdapter;
import com.example.music_58.ui.main_play.MainPlayActivity;
import com.example.music_58.util.StringUtils;

import java.util.List;

public class SearchActivity extends BaseLoadMoreActivity
        implements SearchContract.View, View.OnClickListener, TrackAdapter.OnTrackClickListener,
        SearchView.OnQueryTextListener {
    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private TrackAdapter mTrackAdapter;
    private SearchContract.Presenter mPresenter;
    private List<Track> mTracks;
    private MediaPlayerService mService;
    private int mOffset;
    private String mKeyword;
    private Handler mHandler;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) iBinder;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService(mConnection);
        }
    };

    public static Intent getIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initPresenter();
        initViewLoadMore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) unbindService(mConnection);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void loadMoreData() {
        mIsScrolling = false;
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.searchTracks(StringUtils.initSearchApi(mKeyword, ++mOffset));
    }

    @Override
    public void initViewLoadMore() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mTrackAdapter = new TrackAdapter(this, this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mTrackAdapter);
        mProgressBar.setVisibility(View.GONE);
        setLoadMore();
    }

    @Override
    public void showResult(List<Track> tracks) {
        Intent serviceIntent = MediaPlayerService.getPlayMusicServiceIntent(SearchActivity.this);
        if (mService == null)
            bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
        if (mOffset == 0) {
            mTrackAdapter.updateTracks(tracks);
            mTracks = tracks;
            return;
        }
        mTrackAdapter.addTracks(tracks);
        mTracks.addAll(tracks);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoResult(String message) {

    }

    @Override
    public void onTrackClick(int position) {
        if (mService != null) {
            startActivity(MainPlayActivity.getIntent(this));
            mService.setTracks(mTracks);
            mService.requestCreate(position);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mKeyword = s;
        mOffset = 0;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String test = StringUtils.initSearchApi(mKeyword, 0);
                mPresenter.searchTracks(test);
            }
        }, 500);
        return true;
    }

    private void initPresenter() {
        TrackRepository repository = TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(getApplicationContext())
        );
        mPresenter = new SearchPresenter(repository, this);
    }

    private void initView() {
        mSearchView = findViewById(R.id.search_view);
        mProgressBar = findViewById(R.id.progress_search_more);
        mRecyclerView = findViewById(R.id.recycler_search);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(false);
        mHandler = new Handler();
    }
}
