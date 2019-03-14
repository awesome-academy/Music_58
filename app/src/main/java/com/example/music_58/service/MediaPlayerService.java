package com.example.music_58.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.example.music_58.data.model.Track;
import com.example.music_58.mediaplayer.MediaPlayerManager;
import com.example.music_58.mediaplayer.MediaRequest;
import com.example.music_58.util.Constants;

import java.util.List;

public class MediaPlayerService extends Service implements MediaPlayerManager.OnLoadingTrackListener,
        MediaPlayerController {
    public static final String EXTRA_REQUEST_CODE = "REQUEST_CODE";
    private static final String WORKER_THREAD_NAME = "ServiceStartArguments";
    private static Handler mUIHandler;
    private final IBinder mBinder = new LocalBinder();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private MediaPlayerManager mMediaPlayerManager;

    public static void setUIHandler(Handler uiHandler) {
        mUIHandler = uiHandler;
    }

    public static Intent getPlayMusicServiceIntent(Context context) {
        return new Intent(context, MediaPlayerService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerManager = MediaPlayerManager.getsInstance(this, this);
        HandlerThread thread = new HandlerThread(WORKER_THREAD_NAME);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int request = intent.getIntExtra(EXTRA_REQUEST_CODE, 0);
            switch (request) {
                case NotificationAction.VALUE_NEXT_SONG:
                    requestChangeSong(Constants.NEXT_SONG);
                    break;
                case NotificationAction.VALUE_PREVIOUS_SONG:
                    requestChangeSong(Constants.PREVIOUS_SONG);
                    break;
                case NotificationAction.VALUE_PLAY_SONG:
                    if (isPlaying()) {
                        requestPause();
                    } else {
                        requestStart();
                    }
                    break;
                default:
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onStartLoading(int index) {
        if (mUIHandler != null) {
            Message message = new Message();
            message.arg1 = index;
            message.what = MediaRequest.LOADING;
            mUIHandler.sendMessage(message);
        }
    }

    @Override
    public void onLoadingFail(String message) {
        Message msg = new Message();
        msg.what = MediaRequest.FAILURE;
        msg.obj = message;
        mUIHandler.sendMessage(msg);
    }

    @Override
    public void onLoadingSuccess() {
        mUIHandler.sendEmptyMessage(MediaRequest.SUCCESS);
    }

    @Override
    public void onTrackPaused() {
        mUIHandler.sendEmptyMessage(MediaRequest.PAUSED);
    }

    @Override
    public void onTrackStopped() {
        mUIHandler.sendEmptyMessage(MediaRequest.STOPPED);
    }

    @Override
    public void create(int index) {
        mMediaPlayerManager.create(index);
    }

    @Override
    public void prepareAsync() {
        mMediaPlayerManager.prepareAsync();
    }

    @Override
    public void start() {
        mMediaPlayerManager.start();
    }

    @Override
    public void pause() {
        mMediaPlayerManager.pause();
    }

    @Override
    public void stop() {
        mMediaPlayerManager.stop();
    }

    @Override
    public int getDuration() {
        return mMediaPlayerManager.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayerManager.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayerManager.isPlaying();
    }

    @Override
    public void seek(int possition) {
        mMediaPlayerManager.seek(possition);
    }

    @Override
    public void loop(boolean isLoop) {
        mMediaPlayerManager.loop(isLoop);
    }

    @Override
    public int getSong() {
        return mMediaPlayerManager.getCurrentPositionSong();
    }

    @Override
    public void changeSong(int i) {
        mMediaPlayerManager.changeSong(i);
    }

    public void requestChangeSong(int index) {
        Message message = new Message();
        message.what = CHANGE_SONG;
        message.arg1 = index;
        mServiceHandler.sendMessage(message);
    }

    public void requestStart() {
        mServiceHandler.sendEmptyMessage(REQUEST_START);
    }

    public void requestPause() {
        mServiceHandler.sendEmptyMessage(REQUEST_PAUSE);
    }

    public void requestSeek(int i) {
        Message message = new Message();
        message.arg1 = i;
        message.what = REQUEST_SEEK;
        mServiceHandler.sendMessage(message);
    }

    public void requestPrepareAsync() {
        mServiceHandler.sendEmptyMessage(REQUEST_PREPARE_ASYNC);
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayerManager.getMediaPlayer();
    }

    public List<Track> getTracks() {
        return mMediaPlayerManager.getTracks();
    }

    public MediaPlayerService setTracks(List<Track> tracks) {
        mMediaPlayerManager.setTracks(tracks);
        return this;
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mMediaPlayerManager;
    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestState.CREATE:
                    create(msg.arg1);
                    break;
                case RequestState.CHANGE_SONG:
                    changeSong(msg.arg1);
                    break;
                case RequestState.START:
                    start();
                    break;
                case RequestState.PAUSE:
                    pause();
                    break;
                case RequestState.SEEK:
                    seek(msg.arg1);
                    break;
                case RequestState.PREPARE_ASYNC:
                    prepareAsync();
                    break;
                default:
                    break;
            }
        }
    }
}
