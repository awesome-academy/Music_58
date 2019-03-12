package com.example.music_58.data.source.local;

import android.content.Context;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.Local {
    private static TrackLocalDataSource sInstance;

    private TrackLocalDataSource(Context context) {

    }

    public static synchronized TrackLocalDataSource getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new TrackLocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public void getLocalTracks(TrackDataSource.DataCallback<Track> callback) {

    }

    @Override
    public void searchLocalTracks(String keyword, TrackDataSource.DataCallback<Track> callback) {

    }

    @Override
    public void getFavoriteTracks(TrackDataSource.DataCallback<Track> callback) {

    }

    @Override
    public void addFavoriteTrack(Track track, TrackDataSource.DataCallback<String> callback) {

    }

    @Override
    public void deleteFavoriteTrack(Track track, TrackDataSource.DataCallback<Boolean> callback) {

    }

    @Override
    public void getRecentTrack(TrackDataSource.DataCallback<Long> callback) {

    }
}
