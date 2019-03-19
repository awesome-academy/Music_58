package com.example.music_58.ui.search;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.repository.TrackRepository;
import com.example.music_58.data.source.TrackDataSource;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mView;
    private TrackRepository mRepository;

    public SearchPresenter(TrackRepository repository, SearchContract.View view) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void searchTracks(String api) {
        mRepository.searchTracks(api, new TrackDataSource.DataCallback<Track>() {
            @Override
            public void onSuccess(List<Track> data) {
                mView.showResult(data);
            }

            @Override
            public void onFailure(String message) {
                mView.showNoResult(message);
            }
        });
    }
}
