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
                Constants.PARAMETER_LIMIT, String.valueOf(Constants.LIMIT),
                Constants.PARAMETER_OFFSET, String.valueOf(offset));
    }
}
