package com.example.music_58.service;

import android.support.annotation.IntDef;

@IntDef({StatusPlayerType.IDLE, StatusPlayerType.INITIALIZED, StatusPlayerType.PREPARING,
        StatusPlayerType.STARTED, StatusPlayerType.PAUSED, StatusPlayerType.STOPPED,
        StatusPlayerType.END, StatusPlayerType.PLAYBACK_COMPLETED})

public @interface StatusPlayerType {
    int IDLE = 0;
    int INITIALIZED = 1;
    int PREPARING = 2;
    int STARTED = 3;
    int PAUSED = 4;
    int STOPPED = 5;
    int END = 6;
    int PLAYBACK_COMPLETED = 7;
}
