package com.example.music_58.data.repository;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.source.TrackDataSource;

public class TrackRepository implements TrackDataSource.Local, TrackDataSource.Remote {
    private static TrackRepository sInstance;
    private TrackDataSource.Remote mRemoteDataSource;
    private TrackDataSource.Local mLocalDataSource;

    private TrackRepository(TrackDataSource.Remote remoteDataSource,
                            TrackDataSource.Local localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static synchronized TrackRepository getInstance(TrackDataSource.Remote remoteDataSource,
                                                           TrackDataSource.Local localDataSource) {
        if (sInstance == null) {
            sInstance = new TrackRepository(remoteDataSource, localDataSource);
        }
        return sInstance;
    }

    @Override
    public void getLocalTracks(DataCallback<Track> callback) {
        mLocalDataSource.getLocalTracks(callback);
    }

    @Override
    public void searchLocalTracks(String keyword, DataCallback<Track> callback) {
        mLocalDataSource.searchLocalTracks(keyword, callback);
    }

    @Override
    public void getFavoriteTracks(DataCallback<Track> callback) {
        mLocalDataSource.getFavoriteTracks(callback);
    }

    @Override
    public void addFavoriteTrack(Track track, DataCallback<String> callback) {
        mLocalDataSource.addFavoriteTrack(track, callback);
    }

    @Override
    public void deleteFavoriteTrack(Track track, DataCallback<Boolean> callback) {
        mLocalDataSource.deleteFavoriteTrack(track, callback);
    }

    @Override
    public void getRecentTrack(DataCallback<Long> callback) {
        mLocalDataSource.getRecentTrack(callback);
    }

    @Override
    public void getTracks(String api, DataCallback<Track> callback) {
        mRemoteDataSource.getTracks(api, callback);
    }

    @Override
    public void searchTracks(String api, DataCallback<Track> callback) {
        mRemoteDataSource.searchTracks(api, callback);
    }

    @Override
    public void getDetailTrack(String api, DataCallback<Track> callback) {
        mRemoteDataSource.getDetailTrack(api, callback);
    }
}
