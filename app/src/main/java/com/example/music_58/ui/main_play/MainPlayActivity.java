package com.example.music_58.ui.main_play;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_58.R;
import com.example.music_58.data.model.Track;
import com.example.music_58.util.Constants;

public class MainPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int DURATION = 30000;
    private static final int NUMBER_0 = 0;
    private static final int NUMBER_360 = 360;
    private TextView mTextTrackName;
    private TextView mTextArtist;
    private ImageView mImageBack;
    private ImageView mImageArtwork;
    private ImageView mImagePlay;
    private ImageView mImageDownload;
    private ImageView mImageFavorite;
    private ImageView mImageShuffle;
    private ImageView mImageLoop;

    public static Intent getIntent(Context context, Track track) {
        Intent intent = new Intent(context, MainPlayActivity.class);
        intent.putExtra(Constants.EXTRA_TRACK, track);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_play);
        initView();
        getData();
        handleEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
        }
    }

    private void handleEvent() {
        mImageBack.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
    }

    private void initView() {
        mTextTrackName = findViewById(R.id.text_track_name);
        mImageBack = findViewById(R.id.image_back);
        mTextArtist = findViewById(R.id.text_artist);
        mImageArtwork = findViewById(R.id.image_artwork);
        mImagePlay = findViewById(R.id.image_play);
        mImageDownload = findViewById(R.id.image_download);
        mImageFavorite = findViewById(R.id.image_favorite);
        mImageLoop = findViewById(R.id.image_loop);
        mImageShuffle = findViewById(R.id.image_shuffle);
    }

    private void initAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImageArtwork,
                Constants.EXTRA_ROTATION, NUMBER_0, NUMBER_360);
        animator.setDuration(DURATION);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private void getData() {
        Intent intent = getIntent();
        Track track = intent.getParcelableExtra(Constants.EXTRA_TRACK);
        mTextTrackName.setText(track.getTitle());
        mTextArtist.setText(track.getArtist());
        Glide.with(this).load(track.getArtworkUrl())
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.genre_all)
                .into(mImageArtwork);
    }
}
