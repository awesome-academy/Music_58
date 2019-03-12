package com.example.music_58.service;

import android.support.annotation.IntDef;

@IntDef ({
        RequestState.CREATE,
        RequestState.CHANGE_SONG,
        RequestState.START,
        RequestState.PAUSE,
        RequestState.SEEK,
        RequestState.PREPARE_ASYNC

})

public @interface RequestState {
    int CREATE = 1001;
    int CHANGE_SONG = 1002;
    int START = 1003;
    int PAUSE = 1004;
    int SEEK = 1005;
    int PREPARE_ASYNC = 1006;
}
