package com.example.music_58.service;

import android.support.annotation.IntDef;

@IntDef({
        NotificationAction.VALUE_NEXT_SONG,
        NotificationAction.VALUE_PREVIOUS_SONG,
        NotificationAction.VALUE_PAUSE_SONG,
        NotificationAction.VALUE_PLAY_SONG
})

public @interface NotificationAction {
    int VALUE_NEXT_SONG = 9000;
    int VALUE_PREVIOUS_SONG = 9100;
    int VALUE_PAUSE_SONG = 9200;
    int VALUE_PLAY_SONG = 9300;
}
