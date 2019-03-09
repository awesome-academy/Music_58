package com.example.music_58.data.source.remote;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.source.TrackDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackAsyncTask extends BaseAsyncTask<Track> {
    private static final String ARTWORK_URL = "artwork_url";
    private static final String COLLECTION = "collection";
    private static final String ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_NAME = "username";
    private static final String TITLE = "title";
    private static final String TRACK = "track";
    private static final String DOWNLOADABLE = "downloadable";

    public TrackAsyncTask(TrackDataSource.DataCallback<Track> callback) {
        super(callback);
    }

    @Override
    public List<Track> convertJSON(String respond) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONObject result = new JSONObject(respond);
            JSONArray collection = result.getJSONArray(COLLECTION);
            for (int i = 0; i < collection.length(); i++) {
                JSONObject trackInfo = collection.getJSONObject(i);
                JSONObject track = trackInfo.getJSONObject(TRACK);
                int id = track.getInt(ID);
                String title = track.getString(TITLE);
                String artworkUrl = track.getString(ARTWORK_URL);
                String artist = track.getJSONObject(KEY_USER).getString(KEY_USER_NAME);
                boolean isDownloadable = track.getBoolean(DOWNLOADABLE);
                Track object = new Track(id, title, artist);
                object.setArtworkUrl(artworkUrl);
                object.setDownloadable(isDownloadable);
                tracks.add(object);
            }
        } catch (JSONException e) {
            mException = e;
        }
        return tracks;
    }
}
