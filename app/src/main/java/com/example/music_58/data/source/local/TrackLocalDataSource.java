package com.example.music_58.data.source.local;

import android.content.Context;
import android.os.AsyncTask;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.Local {
    private static TrackLocalDataSource sInstance;
    private FavoriteDbManager mDbManager;
    private Context mContext;

    private TrackLocalDataSource(Context context) {
        mContext = context;
        mDbManager = new FavoriteDbManager(context);
    }

    public static synchronized TrackLocalDataSource getInstance(Context context) {
        if (sInstance == null)
            sInstance = new TrackLocalDataSource(context);
        return sInstance;
    }

    @Override
    public void getLocalTracks(TrackDataSource.DataCallback<Track> callback) {
        new LocalTrackManager(mContext.getContentResolver(), false, callback)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void searchLocalTracks(String keyword, TrackDataSource.DataCallback<Track> callback) {

    }

    @Override
    public boolean isAddedToFavorite(Track track) {
        return mDbManager.isFavoriteTrack(track);
    }

    @Override
    public void getFavoriteTracks(TrackDataSource.DataCallback<Track> callback) {
        mDbManager.getTracks(callback);
    }

    @Override
    public void addFavoriteTrack(Track track, TrackDataSource.DataCallback<String> callback) {
        mDbManager.insertTrack(track, callback);
    }

    @Override
    public void deleteFavoriteTrack(Track track, TrackDataSource.DataCallback<String> callback) {
        mDbManager.deleteTrack(track, callback);
    }
}
