package com.example.music_58.data.source.remote;

import com.example.music_58.data.model.Track;
import com.example.music_58.data.source.TrackDataSource;
import com.example.music_58.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchTrackAsyncTask extends BaseAsyncTask<Track> {
    public SearchTrackAsyncTask(TrackDataSource.DataCallback<Track> callback) {
        super(callback);
    }

    @Override
    public List<Track> convertJSON(String respond) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONArray result = new JSONArray(respond);
            for (int i = 0; i < result.length(); i++) {
                JSONObject trackInfo = result.getJSONObject(i);
                JSONObject user = trackInfo.getJSONObject(TrackProperty.USER);
                int id = trackInfo.getInt(TrackProperty.ID);
                String title = trackInfo.getString(TrackProperty.TITLE);
                String streamURL = StringUtils.initStreamApi(id);
                String avatarUrl = user.getString(TrackProperty.AVATAR_URL);
                String artworkUrl;
                if (trackInfo.isNull(TrackProperty.ARTWORK_URL)) {
                    artworkUrl = avatarUrl;
                } else {
                    artworkUrl = trackInfo.getString(TrackProperty.ARTWORK_URL);
                }
                String artist = null;
                if (!trackInfo.isNull(TrackProperty.PUBLISHER_METADATA)
                        && !trackInfo.getJSONObject(TrackProperty.PUBLISHER_METADATA)
                        .isNull(TrackProperty.ARTIST)) {
                    artist = trackInfo.getJSONObject(TrackProperty.PUBLISHER_METADATA)
                            .getString(TrackProperty.ARTIST);
                }
                if (artist == null || artist.equals(""))
                    artist = trackInfo.getJSONObject(TrackProperty.USER).
                            getString(TrackProperty.KEY_USER_NAME);
                boolean isDownloadable = trackInfo.getBoolean(TrackProperty.DOWNLOADABLE);
                String downloadUrl = null;
                if (isDownloadable) {
                    downloadUrl = StringUtils.initDownloadApi(trackInfo
                            .getString(TrackProperty.DOWNLOAD_URL));
                }
                Track object = new Track(id, title, artist);
                object.setArtworkUrl(artworkUrl);
                object.setDownloadable(isDownloadable);
                object.setDownloadUrl(downloadUrl);
                object.setStreamUrl(streamURL);
                tracks.add(object);
            }
        } catch (JSONException e) {
            mException = e;
        }
        return tracks;
    }
}
