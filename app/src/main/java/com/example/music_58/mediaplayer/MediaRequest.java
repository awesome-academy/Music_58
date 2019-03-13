package com.example.music_58.mediaplayer;

import android.support.annotation.IntDef;

@IntDef({
        MediaRequest.LOADING, MediaRequest.SUCCESS,
        MediaRequest.UPDATE_PLAY_ACTIVITY, MediaRequest.UPDATE_MINI_PLAYER,
        MediaRequest.FAILURE, MediaRequest.PAUSED, MediaRequest.STOPPED
})
public @interface MediaRequest {
    int LOADING = 1;
    int SUCCESS = 2;
    int UPDATE_PLAY_ACTIVITY = 3;
    int UPDATE_MINI_PLAYER = 4;
    int FAILURE = 5;
    int PAUSED = 6;
    int STOPPED = 7;
}
