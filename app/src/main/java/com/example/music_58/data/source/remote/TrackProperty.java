package com.example.music_58.data.source.remote;

import android.support.annotation.StringDef;

import com.example.music_58.R;

@StringDef({
        TrackProperty.ARTWORK_URL,
        TrackProperty.COLLECTION,
        TrackProperty.KEY_USER,
        TrackProperty.KEY_USER_NAME,
        TrackProperty.TITLE,
        TrackProperty.TRACK,
        TrackProperty.DOWNLOADABLE,
        TrackProperty.DOWNLOAD_URL,
        TrackProperty.AVATAR_URL,
        TrackProperty.ID,
        TrackProperty.USER
})

public @interface TrackProperty {
    String ARTWORK_URL = "artwork_url";
    String AVATAR_URL = "avatar_url";
    String DOWNLOAD_URL = "download_url";
    String DOWNLOADABLE = "downloadable";
    String TRACK = "track";
    String TITLE = "title";
    String KEY_USER_NAME = "username";
    String KEY_USER = "user";
    String COLLECTION = "collection";
    String ID = "id";
    String USER = "user";
}
