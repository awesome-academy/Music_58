package com.example.music_58.data.model;

import android.support.annotation.StringDef;

@StringDef({
        GenreKey.ALL, GenreKey.AUDIO, GenreKey.ALTERNATIVE,
        GenreKey.AMBIENT, GenreKey.CLASSICAL, GenreKey.COUNTRY
})
public @interface GenreKey {
    String ALL = "soundcloud:genres:all-music";
    String AUDIO = "soundcloud:genres:all-audio";
    String ALTERNATIVE = "soundcloud:genres:alternativerock";
    String AMBIENT = "soundcloud:genres:ambient";
    String CLASSICAL = "soundcloud:genres:classical";
    String COUNTRY = "soundcloud:genres:country";
}
