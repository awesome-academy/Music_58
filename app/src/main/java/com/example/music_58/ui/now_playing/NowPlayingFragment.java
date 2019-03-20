package com.example.music_58.ui.now_playing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_58.R;
import com.example.music_58.data.model.Track;
import com.example.music_58.service.MediaPlayerService;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingFragment extends BottomSheetDialogFragment implements
        PlayingListAdapter.TrackClickListener {
    private RecyclerView mRecyclerNowPlaying;
    private List<Track> mTracks = new ArrayList<>();
    private MediaPlayerService mService;
    private TextView mTextTracksCount;
    private ServiceConnection mConnection;
    private LinearLayoutManager mLayoutManager;
    private PlayingListAdapter mListAdapter;
    private ItemTouchHelper mItemTouchHelper;

    public NowPlayingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        initUI(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        getActivity().unbindService(mConnection);
        super.onDestroyView();
    }

    public void setService(MediaPlayerService service) {
        mService = service;
    }

    @Override
    public void onTrackClick(int position) {
        mService.setTracks(mTracks);
        mService.requestCreate(position);
    }

    @Override
    public void onRemoveTrackClick(Track track) {
        int position = mTracks.indexOf(track);
        mTracks.remove(track);
        mListAdapter.notifyItemRemoved(position);
        mListAdapter.notifyItemRangeChanged(position, mTracks.size());
        mService.removeTrack(track);
        mTextTracksCount.setText("Now Playing (" + mTracks.size() + ")");
    }

    private void bindData(List<Track> tracks) {
        mTracks = tracks;
        mTextTracksCount.setText("Now Playing (" + tracks.size() + ")");
        mListAdapter = new PlayingListAdapter(mTracks, this);
        mListAdapter.setTrackPlaying(mService.getTracks().get(mService.getCurrentPositionSong()));
        mLayoutManager.scrollToPosition(mService.getTracks()
                .indexOf(mService.getTracks().get(mService.getCurrentPositionSong())));
        mRecyclerNowPlaying.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();

        ItemTouchHelper.Callback callback = new MyItemTouchCallback(mListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerNowPlaying);
    }

    private void initUI(View view) {
        mTextTracksCount = view.findViewById(R.id.text_tracks_count);
        mRecyclerNowPlaying = view.findViewById(R.id.recycler_now_playing);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerNowPlaying.setLayoutManager(mLayoutManager);

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MediaPlayerService.LocalBinder myBinder = (MediaPlayerService.LocalBinder) service;
                mService = myBinder.getService();
                bindData(mService.getTracks());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = MediaPlayerService.getPlayMusicServiceIntent(getContext());
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
}
