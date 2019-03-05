package com.example.music_58.ui.genre_detail;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.repository.TrackRepository;
import com.example.music_58.data.source.TrackDataSource;

import java.util.List;

public class GenreDetailPresenter implements GenreDetailContract.Presenter {
    private GenreDetailContract.View mView;
    private TrackRepository mRepository;

    public GenreDetailPresenter(TrackRepository repository, GenreDetailContract.View view) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void getTracks(String api) {
        mRepository.getTracks(api, new TrackDataSource.DataCallback<Track>() {
            @Override
            public void onSuccess(List<Track> tracks) {
                mView.onLoadTracksSuccess(tracks);
            }

            @Override
            public void onFailure(String message) {
                mView.onLoadTracksFail(message);
            }
        });
    }
}
