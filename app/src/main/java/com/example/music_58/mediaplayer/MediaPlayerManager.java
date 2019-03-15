package com.example.music_58.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.music_58.data.model.Track;
import com.example.music_58.service.MediaPlayerController;
import com.example.music_58.service.StatusPlayerType;
import com.example.music_58.util.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MediaPlayerManager extends MediaPlayerSetting
        implements MediaPlayerController, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    private static final int INDEX = 1;
    private static MediaPlayerManager sInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private List<Track> mTracks;
    private int mCurrentIndex;
    private int mStatus;
    private OnLoadingTrackListener mListener;

    private MediaPlayerManager() {

    }

    private MediaPlayerManager(Context context, OnLoadingTrackListener listener) {
        mContext = context;
        mListener = listener;
        mLoopType = LoopType.NONE;
        mShuffleType = ShuffleType.OFF;
    }

    public static MediaPlayerManager getsInstance(Context context,
                                                  OnLoadingTrackListener listener) {
        if (sInstance == null) {
            sInstance = new MediaPlayerManager(context, listener);
        }
        return sInstance;
    }

    @Override
    public void create(int index) {
        mCurrentIndex = index;
        Track track = mTracks.get(mCurrentIndex);
        mListener.onStartLoading(index);
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mStatus = StatusPlayerType.IDLE;
        } else {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
        }
        if (!mTracks.isEmpty()) {
            initOnline(track);
        }
    }

    private void initOnline(Track track) {
        Uri uri = Uri.parse(track.getStreamUrl());
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mContext, uri);
            mStatus = StatusPlayerType.INITIALIZED;
            prepareAsync();
        } catch (IOException e) {
            mListener.onLoadingFail(e.getMessage());
        }
    }

    @Override
    public void prepareAsync() {
        if (mMediaPlayer != null) {
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(this);
            mStatus = StatusPlayerType.PREPARING;
        }
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mStatus = StatusPlayerType.STARTED;
            mListener.onLoadingSuccess();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mStatus = StatusPlayerType.PAUSED;
            mListener.onTrackPaused();
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mStatus = StatusPlayerType.STOPPED;
            mListener.onTrackStopped();
        }
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null && mStatus > StatusPlayerType.PREPARING) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null && mStatus != StatusPlayerType.IDLE) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public void seek(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public void loop(boolean isLoop) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(isLoop);
        }
    }

    @Override
    public int getCurrentPositionSong() {
        return mMediaPlayer != null ? mCurrentIndex : 0;
    }

    @Override
    public void changeSong(int index) {
        if (mShuffleType == MediaPlayerSetting.ShuffleType.ON) {
            index = randomSong();
        }
        mCurrentIndex += index;
        if (mCurrentIndex >= mTracks.size()) {
            mCurrentIndex = 0;
        } else if (mCurrentIndex < 0) {
            mCurrentIndex = mTracks.size() - INDEX;
        }
        create(mCurrentIndex);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        switch (mLoopType) {
            case LoopType.NONE:
                if (mCurrentIndex == mTracks.size() - INDEX
                        && mStatus != StatusPlayerType.STOPPED) {
                    stop();
                } else {
                    changeSong(Constants.NEXT_SONG);
                }
                break;
            case LoopType.ALL:
                changeSong(Constants.NEXT_SONG);
                break;
            case LoopType.ONE:
                start();
                break;
            default:
                break;
        }
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public MediaPlayerManager setTracks(List<Track> tracks) {
        mTracks = tracks;
        return this;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public int getStatus() {
        return mStatus;
    }

    public MediaPlayerManager setStatus(int status) {
        this.mStatus = status;
        return this;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        start();
    }

    private int randomSong() {
        int result = 0;
        int currentPositionSong = getCurrentPositionSong();
        int maxSong = getTracks().size() - INDEX;
        Random r = new Random();
        result = r.nextInt((maxSong - currentPositionSong + INDEX));
        return result;
    }

    public interface OnLoadingTrackListener {
        void onStartLoading(int index);

        void onLoadingFail(String message);

        void onLoadingSuccess();

        void onTrackPaused();

        void onTrackStopped();
    }
}
