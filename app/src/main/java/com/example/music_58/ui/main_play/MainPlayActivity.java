package com.example.music_58.ui.main_play;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_58.R;
import com.example.music_58.data.model.Track;
import com.example.music_58.data.repository.TrackRepository;
import com.example.music_58.data.source.local.TrackLocalDataSource;
import com.example.music_58.data.source.remote.TrackRemoteDataSource;
import com.example.music_58.mediaplayer.MediaRequest;
import com.example.music_58.service.MediaPlayerService;
import com.example.music_58.service.StatusPlayerType;
import com.example.music_58.ui.now_playing.NowPlayingFragment;
import com.example.music_58.util.Constants;
import com.example.music_58.util.TimeUtils;

import java.util.List;

public class MainPlayActivity extends AppCompatActivity implements View.OnClickListener,
        MainPlayContract.View, SeekBar.OnSeekBarChangeListener, ServiceConnection {
    public static final String ARTWORK_DEFAULT_SIZE = "large";
    public static final String ARTWORK_MAX_SIZE = "t500x500";
    private static final int DURATION = 30000;
    private static final int NUMBER_0 = 0;
    private static final int NUMBER_360 = 360;
    private static final int UPDATE_FOLLOWING_SERVICE = 100;
    private static final int MESSAGE_UPDATE_DELAY = 1000;
    private ServiceConnection mConnection;
    private TextView mTextTrackName;
    private TextView mTextArtist;
    private TextView mTextCurrentTime;
    private TextView mTextDuration;
    private ImageView mImageBack;
    private ImageView mImageArtwork;
    private ImageView mImagePlay;
    private ImageView mImageDownload;
    private ImageView mImageFavorite;
    private ImageView mImageShuffle;
    private ImageView mImageLoop;
    private ImageView mImageNext;
    private ImageView mImagePrev;
    private ImageView mImageTracksPlaying;
    private SeekBar mSeekBar;
    private MediaPlayerService mService;
    private boolean mIsBoundService;
    private MainPlayContract.Presenter mPresenter;
    private ObjectAnimator mAnimator;
    private Track mTrack;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case MediaRequest.LOADING:
                    if (mIsBoundService) updateUI(message.arg1);
                    startLoading(message.arg1);
                    break;
                case MediaRequest.SUCCESS:
                    loadingSuccess();
                    break;
                case MediaRequest.UPDATE_PLAY_ACTIVITY:
                    updateUI(message.arg1);
                    break;
                case MediaRequest.UPDATE_MINI_PLAYER:
                    break;
                case MediaRequest.PAUSED:
                    mImagePlay.setImageResource(R.drawable.ic_play_circle_outline_white);
                    break;
                case MediaRequest.FAILURE:
                    Toast.makeText(MainPlayActivity.this, (String) message.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MediaRequest.STOPPED:
                    mImagePlay.setImageResource(R.drawable.ic_play_circle_outline_white);
                    break;
                case UPDATE_FOLLOWING_SERVICE:
                    updateSeekBar();
                default:
                    break;
            }
        }
    };

    public static Intent getIntent(Context context) {
        return new Intent(context, MainPlayActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_play);
        initView();
        handleEvent();
        initPresenter();
        if (mService == null) startService(MediaPlayerService.getPlayMusicServiceIntent(this));
        bindPlayMusicService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.image_play:
                playSong();
                break;
            case R.id.image_next:
                mService.requestChangeSong(Constants.NEXT_SONG);
                break;
            case R.id.image_previous:
                mService.requestChangeSong(Constants.PREVIOUS_SONG);
                break;
            case R.id.image_tracks_playing:
                NowPlayingFragment.getInstance().show(getSupportFragmentManager(),
                        NowPlayingFragment.getInstance().getTag());
                break;
            default:
                break;
        }
    }

    @Override
    public void onAddTracksFailure(String message) {

    }

    @Override
    public void onAddTracksSuccess(List<String> id) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mService.requestSeek(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) iBinder;
        mService = binder.getService();
        mService.setUIHandler(mHandler);
        requestUpdateUI();
        mIsBoundService = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBoundService) {
            mIsBoundService = false;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    private void requestUpdateUI() {
        if (mService != null && mService.getMediaPlayer() != null) {
            updateUIAndDuration();
        }
        Message message = new Message();
        message.what = MediaRequest.UPDATE_PLAY_ACTIVITY;
        message.arg1 = mService.getCurrentPositionSong();
        mHandler.sendMessage(message);
    }

    private void updateUI(int index) {
        mTrack = mService.getTracks().get(index);
        mTextTrackName.setText(mTrack.getTitle());
        mTextArtist.setText(mTrack.getArtist());
        String image = mTrack.getArtworkUrl()
                .replace(ARTWORK_DEFAULT_SIZE, ARTWORK_MAX_SIZE);
        Glide.with(mImageArtwork)
                .load(image)
                .apply(new RequestOptions().circleCrop())
                .into(mImageArtwork);
    }

    private void updateUIAndDuration() {
        int index = mService.getCurrentPositionSong();
        Track track = mService.getTracks().get(index);
        int duration = mService.getDuration();
        String image = track.getArtworkUrl()
                .replace(ARTWORK_DEFAULT_SIZE, ARTWORK_MAX_SIZE);
        Glide.with(mImageArtwork)
                .load(image)
                .apply(new RequestOptions().circleCrop())
                .into(mImageArtwork);
        mTextTrackName.setText(track.getTitle());
        mTextArtist.setText(track.getArtist());
        mSeekBar.setMax(mService.getDuration());
        mSeekBar.setProgress(mService.getCurrentPosition());
        mTextDuration.setText(TimeUtils.convertMilisecondToFormatTime(duration));
        updatePlayImage(mService.isPlaying());
        mHandler.sendEmptyMessageDelayed(UPDATE_FOLLOWING_SERVICE,
                MESSAGE_UPDATE_DELAY);
    }

    private void updatePlayImage(boolean isPlaying) {
        if (isPlaying) {
            mImagePlay.setImageResource(R.drawable.ic_pause_white);
        } else {
            mImagePlay.setImageResource(R.drawable.ic_play_circle_outline_white);
        }
    }

    private void playSong() {
        if (mService.isPlaying()) {
            mService.requestPause();
            mImagePlay.setImageResource(R.drawable.ic_pause_white);
            return;
        }
        mImagePlay.setImageResource(R.drawable.ic_play_circle_outline_white);
        int mediaStatus = mService.getMediaPlayerManager().getStatus();
        if (mediaStatus == StatusPlayerType.STOPPED) {
            mService.requestPrepareAsync();
            return;
        }
        mService.requestStart();
    }

    private void bindPlayMusicService() {
        mConnection = this;
        bindService(MediaPlayerService.getPlayMusicServiceIntent(MainPlayActivity.this),
                mConnection, BIND_AUTO_CREATE);
    }

    private void loadingSuccess() {
        mSeekBar.setEnabled(true);
        int duration = mService.getDuration();
        mImagePlay.setClickable(true);
        mImagePlay.setImageResource(R.drawable.ic_pause_white);
        mSeekBar.setMax(duration);
        mTextDuration.setText(TimeUtils.convertMilisecondToFormatTime(duration));
    }

    private void startLoading(int index) {
        mSeekBar.setEnabled(false);
        mImagePlay.setClickable(false);
        Track track = mService.getTracks().get(index);
        mTextArtist.setText(track.getArtist());
        mTextTrackName.setText(track.getTitle());
        mSeekBar.setProgress(0);
        mTextCurrentTime.setText(TimeUtils.convertMilisecondToFormatTime(0));
        mTextDuration.setText(TimeUtils.convertMilisecondToFormatTime(0));
    }

    private void updateSeekBar() {
        int currentPosition = mService.getCurrentPosition();
        mSeekBar.setProgress(currentPosition);
        mTextCurrentTime.setText(TimeUtils.convertMilisecondToFormatTime(currentPosition));
        mHandler.sendEmptyMessageDelayed(UPDATE_FOLLOWING_SERVICE,
                MESSAGE_UPDATE_DELAY);
    }

    private void handleEvent() {
        mImageBack.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImagePrev.setOnClickListener(this);
        mImageTracksPlaying.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void initView() {
        mTextTrackName = findViewById(R.id.text_track_name);
        mTextArtist = findViewById(R.id.text_artist);
        mTextCurrentTime = findViewById(R.id.text_current_time);
        mTextDuration = findViewById(R.id.text_duration);
        mImageBack = findViewById(R.id.image_back);
        mImageArtwork = findViewById(R.id.image_artwork);
        mImagePlay = findViewById(R.id.image_play);
        mImageDownload = findViewById(R.id.image_download);
        mImageFavorite = findViewById(R.id.image_favorite);
        mImageLoop = findViewById(R.id.image_loop);
        mImageShuffle = findViewById(R.id.image_shuffle);
        mImageNext = findViewById(R.id.image_next);
        mImagePrev = findViewById(R.id.image_previous);
        mSeekBar = findViewById(R.id.seek_time);
        mImageTracksPlaying = findViewById(R.id.image_tracks_playing);
    }

    private void initPresenter() {
        TrackRepository repository = TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(getApplicationContext())
        );
        mPresenter = new MainPlayPresenter(repository, this);
    }

    private void initAnimation() {
        mAnimator = ObjectAnimator.ofFloat(mImageArtwork,
                Constants.EXTRA_ROTATION, NUMBER_0, NUMBER_360);
        mAnimator.setDuration(DURATION);
        mAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
    }
}
