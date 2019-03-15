package com.example.music_58.ui.now_playing;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_58.R;
import com.example.music_58.data.model.Track;
import com.example.music_58.service.MediaPlayerService;
import com.example.music_58.ui.adapter.TrackAdapter;

import java.util.List;

public class NowPlayingFragment extends BottomSheetDialogFragment implements View.OnClickListener,
        TrackAdapter.OnTrackClickListener {
    private RecyclerView mRecyclerNowPlaying;
    private ImageView mImageBack;
    private List<Track> mTracks;
    private TrackAdapter mAdapter;
    private MediaPlayerService mService;
    private TextView mTextTracksCount;

    public NowPlayingFragment() {

    }

    public static NowPlayingFragment getInstance() {
        return new NowPlayingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mService != null) {
            mTracks = mService.getTracks();
            mRecyclerNowPlaying.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new TrackAdapter(getContext(), mTracks, this);
            mRecyclerNowPlaying.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTrackClick(int position) {

    }

    private void initView() {

    }
}
