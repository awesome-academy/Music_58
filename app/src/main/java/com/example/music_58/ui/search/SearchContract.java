package com.example.music_58.ui.search;

import com.example.music_58.data.model.Track;

import java.util.List;

public class SearchContract {
    interface Presenter {
        void searchTracks(String api);
    }

    interface View {
        void showResult(List<Track> tracks);

        void showNoResult(String message);
    }
}
