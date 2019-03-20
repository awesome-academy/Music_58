package com.example.music_58.service;

import com.example.music_58.data.model.Track;

public interface MediaPlayerController {
    void create(int index);

    void prepareAsync();

    void start();

    void pause();

    void stop();

    int getDuration();

    int getCurrentPosition();

    boolean isPlaying();

    void seek(int possition);

    void loop(boolean isLoop);

    int getCurrentPositionSong();

    void changeSong(int i);

    void removeTrack(Track track);
}
