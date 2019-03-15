package com.example.music_58.util;

import com.example.music_58.BuildConfig;
import com.example.music_58.data.model.GenreKey;

public class StringUtils {
    public static String append(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    public static String initGenreApi(@GenreKey String genreKey, int offset) {
        return StringUtils.append(Constants.BASE_URL_GENRES, genreKey,
                Constants.CLIENT_ID, BuildConfig.ApiKey,
                Constants.PARAMETER_LIMIT, String.valueOf(Constants.SONG_LIMIT),
                Constants.PARAMETER_OFFSET, String.valueOf(offset));
    }

    public static String initStreamApi(long trackId) {
        return StringUtils.append(Constants.BASE_URL_TRACK, Constants.SPLASH,
                String.valueOf(trackId), Constants.SPLASH,
                Constants.NAME_STREAM, Constants.QUESTION_MARK,
                Constants.CLIENT_ID, BuildConfig.ApiKey);
    }
}
