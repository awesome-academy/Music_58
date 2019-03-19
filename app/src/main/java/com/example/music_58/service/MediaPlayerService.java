package com.example.music_58.service;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.example.music_58.R;
import com.example.music_58.data.model.Track;
import com.example.music_58.mediaplayer.MediaPlayerManager;
import com.example.music_58.mediaplayer.MediaRequest;
import com.example.music_58.ui.main_play.MainPlayActivity;
import com.example.music_58.util.Constants;

import java.util.List;

public class MediaPlayerService extends Service implements MediaPlayerManager.OnLoadingTrackListener,
        MediaPlayerController {
    public static final String EXTRA_REQUEST_CODE = "REQUEST_CODE";
    private static final String WORKER_THREAD_NAME = "ServiceStartArguments";
    private static final int NOTIFICATION_ID = 1;
    private static Handler mUIHandler;
    private final IBinder mBinder = new LocalBinder();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private MediaPlayerManager mMediaPlayerManager;
    private RemoteViews mNotificationLayout;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent mNextPendingIntent;
    private PendingIntent mPreviousPendingIntent;
    private PendingIntent mPlayPendingIntent;

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
        if (mBuilder == null) {
            createMusicNotification();
        } else {
            updateNotification(index);
        }
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
        updateNotification();
        mUIHandler.sendEmptyMessage(MediaRequest.SUCCESS);
    }

    @Override
    public void removeTrack(Track track) {
        mMediaPlayerManager.removeTrack(track);
    }

    @Override
    public void onTrackPaused() {
        updateNotification();
        mUIHandler.sendEmptyMessage(MediaRequest.PAUSED);
    }

    @Override
    public void onTrackStopped() {
        updateNotification();
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
    public void seek(int position) {
        mMediaPlayerManager.seek(position);
    }

    @Override
    public void loop(boolean isLoop) {
        mMediaPlayerManager.loop(isLoop);
    }

    @Override
    public int getCurrentPositionSong() {
        return mMediaPlayerManager != null ? mMediaPlayerManager.getCurrentPositionSong() : 0;
    }

    @Override
    public void changeSong(int i) {
        mMediaPlayerManager.changeSong(i);
    }

    public void requestCreate(int index) {
        Track track = getTracks().get(index);
        initLayoutNotification(R.layout.layout_notification, track);
        Message message = new Message();
        message.what = RequestState.CREATE;
        message.arg1 = index;
        mServiceHandler.sendMessage(message);
        createNextPendingIntent();
        createPreviousPendingIntent();
        createPlayPendingIntent();
    }

    public void requestChangeSong(int index) {
        Message message = new Message();
        message.what = RequestState.CHANGE_SONG;
        message.arg1 = index;
        mServiceHandler.sendMessage(message);
    }

    public void requestStart() {
        mServiceHandler.sendEmptyMessage(RequestState.START);
    }

    public void requestPause() {
        mServiceHandler.sendEmptyMessage(RequestState.PAUSE);
    }

    public void requestSeek(int i) {
        Message message = new Message();
        message.arg1 = i;
        message.what = RequestState.SEEK;
        mServiceHandler.sendMessage(message);
    }

    public void requestPrepareAsync() {
        mServiceHandler.sendEmptyMessage(RequestState.PREPARE_ASYNC);
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

    private void initLayoutNotification(int resourceLayout, Track track) {
        mNotificationLayout = new RemoteViews(getPackageName(), resourceLayout);
        mNotificationLayout.setTextViewText(R.id.text_track_name_mini, track.getTitle());
        mNotificationLayout.setTextViewText(R.id.text_artist_mini, track.getArtist());
        mNotificationLayout.setImageViewResource(R.id.image_play_mini, R.drawable.ic_pause_white);
        mNotificationLayout.setViewVisibility(R.id.image_play_mini, View.INVISIBLE);
    }

    private void createMusicNotification() {
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_album_white)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContent(mNotificationLayout);
        Intent resultIntent = new Intent(this, MainPlayActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainPlayActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPenddingIntent = stackBuilder.getPendingIntent(
                        0, PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPenddingIntent);
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    private void updateNotification(int index) {
        Track track = getTracks().get(index);
        mNotificationLayout.setTextViewText(R.id.text_track_name_mini, track.getTitle());
        mNotificationLayout.setTextViewText(R.id.text_artist_mini, track.getArtist());
        if (isPlaying()) {
            mNotificationLayout.setImageViewResource(R.id.image_play_mini, R.drawable.ic_pause_white);
            mBuilder.setOngoing(false);
        } else {
            mNotificationLayout.setImageViewResource(R.id.image_play_mini, R.drawable.ic_play_white_24dp);
            mBuilder.setOngoing(true);
        }
        mNotificationLayout.setViewVisibility(R.id.image_play_mini, View.INVISIBLE);
        mBuilder.setContent(mNotificationLayout);
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    private void updateNotification() {
        mNotificationLayout.setViewVisibility(R.id.image_play_mini, View.VISIBLE);
        if (!isPlaying()) {
            mNotificationLayout.setImageViewResource(R.id.image_play_mini, R.drawable.ic_play_white_24dp);
            mBuilder.setOngoing(false);
            mBuilder.setContent(mNotificationLayout);
            startForeground(NOTIFICATION_ID, mBuilder.build());
            stopForeground(false);
        } else {
            mNotificationLayout.setImageViewResource(R.id.image_play_mini, R.drawable.ic_pause_white);
            mBuilder.setContent(mNotificationLayout);
            startForeground(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void createNextPendingIntent() {
        Intent nextIntent = new Intent(getApplicationContext(), MediaPlayerService.class);
        nextIntent.putExtra(EXTRA_REQUEST_CODE, NotificationAction.VALUE_NEXT_SONG);
        mNextPendingIntent = PendingIntent.getService(getApplicationContext(),
                NotificationAction.VALUE_NEXT_SONG, nextIntent, 0);
        mNotificationLayout.setOnClickPendingIntent(R.id.image_next_mini, mNextPendingIntent);
    }

    private void createPreviousPendingIntent() {
        Intent nextIntent = new Intent(getApplicationContext(), MediaPlayerService.class);
        nextIntent.putExtra(EXTRA_REQUEST_CODE, NotificationAction.VALUE_PREVIOUS_SONG);
        mPreviousPendingIntent = PendingIntent.getService(getApplicationContext(),
                NotificationAction.VALUE_PREVIOUS_SONG, nextIntent, 0);
        mNotificationLayout.setOnClickPendingIntent(R.id.image_prev_mini, mPreviousPendingIntent);
    }

    private void createPlayPendingIntent() {
        Intent nextIntent = new Intent(getApplicationContext(), MediaPlayerService.class);
        nextIntent.putExtra(EXTRA_REQUEST_CODE, NotificationAction.VALUE_PLAY_SONG);
        mPlayPendingIntent = PendingIntent.getService(getApplicationContext(),
                NotificationAction.VALUE_PLAY_SONG, nextIntent, 0);
        mNotificationLayout.setOnClickPendingIntent(R.id.image_play_mini, mPlayPendingIntent);
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
            super.handleMessage(msg);
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
