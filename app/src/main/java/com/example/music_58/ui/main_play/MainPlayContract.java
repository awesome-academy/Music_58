package com.example.music_58.ui.main_play;

import com.example.music_58.data.model.Track;

import java.util.List;

public class MainPlayContract {
    interface View {
        void onAddTracksFailure(String message);

        void onAddTracksSuccess(List<String> id);
    }

    interface Presenter {
        void addFavoriteTrack(Track track);
    }
}
