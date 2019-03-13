package com.example.music_58.mediaplayer;

import android.support.annotation.IntDef;

public interface MediaPlayerListener {
    @IntDef({State.LOOP, State.PLAYING, State.PAUSED, State.NO_LOOP, State.COMPLETED})
    @interface State {
        int PLAYING = 0;
        int PAUSED = 1;
        int LOOP = 2;
        int NO_LOOP = 3;
        int COMPLETED = 4;
    }
}
