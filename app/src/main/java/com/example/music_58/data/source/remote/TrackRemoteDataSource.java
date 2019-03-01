package com.example.music_58.data.source.remote;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.source.TrackDataSource;

public class TrackRemoteDataSource implements TrackDataSource.Remote {
    private static TrackRemoteDataSource sInstance;

    public static synchronized TrackRemoteDataSource getInstance(){
        if(sInstance == null) {
            sInstance = new TrackRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getTracks(String api, DataCallback<Track> callback) {

    }

    @Override
    public void searchTracks(String api, DataCallback<Track> callback) {

    }

    @Override
    public void getDetailTrack(String api, DataCallback<Track> callback) {

    }
}
