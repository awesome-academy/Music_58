package com.example.music_58.ui.genre_detail;

import com.example.music_58.data.model.Track;

import java.util.List;

public class GenreDetailContract {
    interface View {
        void onLoadTracksSuccess(List<Track> tracks);

        void onLoadTracksFail(String message);
    }

    interface Presenter {
        void getTracks(String api);
    }
}
