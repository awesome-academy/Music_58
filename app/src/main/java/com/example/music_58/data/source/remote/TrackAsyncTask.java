package com.example.music_58.data.source.remote;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.source.TrackDataSource;
import com.example.music_58.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackAsyncTask extends BaseAsyncTask<Track> {
    public TrackAsyncTask(TrackDataSource.DataCallback<Track> callback) {
        super(callback);
    }

    @Override
    public List<Track> convertJSON(String respond) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONObject result = new JSONObject(respond);
            JSONArray collection = result.getJSONArray(TrackProperty.COLLECTION);
            for (int i = 0; i < collection.length(); i++) {
                JSONObject trackInfo = collection.getJSONObject(i);
                JSONObject track = trackInfo.getJSONObject(TrackProperty.TRACK);
                JSONObject user = track.getJSONObject(TrackProperty.USER);
                String avatarUrl = user.getString(TrackProperty.AVATAR_URL);
                int id = track.getInt(TrackProperty.ID);
                String title = track.getString(TrackProperty.TITLE);
                String streamURL = StringUtils.initStreamApi(id);
                String artworkUrl;
                if (track.isNull(TrackProperty.ARTWORK_URL)) {
                    artworkUrl = avatarUrl;
                } else artworkUrl = track.getString(TrackProperty.ARTWORK_URL);
                String artist = track.getJSONObject(TrackProperty.KEY_USER).getString
                        (TrackProperty.KEY_USER_NAME);
                boolean isDownloadable = track.getBoolean(TrackProperty.DOWNLOADABLE);
                String downloadUrl = null;
                if (isDownloadable) {
                    downloadUrl = StringUtils.initDownloadApi(
                            track.getString(TrackProperty.DOWNLOAD_URL));
                }
                Track object = new Track(id, title, artist);
                object.setArtworkUrl(artworkUrl);
                object.setDownloadable(isDownloadable);
                object.setStreamUrl(streamURL);
                object.setDownloadUrl(downloadUrl);
                tracks.add(object);
            }
        } catch (JSONException e) {
            mException = e;
        }
        return tracks;
    }
}
