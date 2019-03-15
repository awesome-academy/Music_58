package com.example.music_58.ui.main_play;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.repository.TrackRepository;
import com.example.music_58.data.source.TrackDataSource;

import java.util.List;

public class MainPlayPresenter implements MainPlayContract.Presenter {
    private MainPlayContract.View mView;
    private TrackRepository mRepository;

    public MainPlayPresenter(TrackRepository repository, MainPlayContract.View view) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void addFavoriteTrack(Track track) {
        mRepository.addFavoriteTrack(track, new TrackDataSource.DataCallback<String>() {
            @Override
            public void onSuccess(List<String> data) {
                mView.onAddTracksSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                mView.onAddTracksFailure(message);
            }
        });
    }
}
