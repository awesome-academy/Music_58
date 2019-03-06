package com.example.music_58.data.model;

import android.support.annotation.StringDef;

@StringDef({
        GenreName.ALL, GenreName.AUDIO, GenreName.ALTERNATIVE,
        GenreName.AMBIENT, GenreName.CLASSICAL, GenreName.COUNTRY
})
public @interface GenreName {
    String ALL = "All music";
    String AUDIO = "Audio";
    String ALTERNATIVE = "Alternative";
    String AMBIENT = "Ambient";
    String CLASSICAL = "Classical";
    String COUNTRY = "Country";
}
