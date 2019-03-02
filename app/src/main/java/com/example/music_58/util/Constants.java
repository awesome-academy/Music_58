package com.example.music_58.util;

public class Constants {
    public static final String BASE_URL_GENRE
            = "https://api-v2.soundcloud.com/charts?kind=%s&genre=%s&client_id=%s&limit=%d&offset=%d";
    public static final String BASE_URL_SEARCH
            = "http://api.soundcloud.com/tracks?q=%s&client_id=%s&limit=%d&offset=%d";
    public static final String BASE_URL_STREAM
            = "https://api.soundcloud.com/tracks/%s/stream?client_id=%s";
    public static final String BASE_URL_DOWNLOAD
            = "https://api.soundcloud.com/tracks/%s/download?client_id=%s";
    public static final String BASE_URL_TRACK =
            "http://api.soundcloud.com/tracks/%s?client_id=%s";
}
