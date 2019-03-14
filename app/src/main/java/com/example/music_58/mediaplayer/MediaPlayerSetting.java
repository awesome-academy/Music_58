package com.example.music_58.mediaplayer;

import android.support.annotation.IntDef;

public class MediaPlayerSetting {
    protected int mLoopType;
    protected int mShuffleType;

    public int getShuffleType() {
        return mShuffleType;
    }

    public MediaPlayerSetting setShuffleType(int shuffleType) {
        mShuffleType = shuffleType;
        return this;
    }

    @LoopType
    public int getLoopType() {
        return mLoopType;
    }

    public void setLoopType(@LoopType int loopType) {
        mLoopType = loopType;
    }

    @IntDef({LoopType.NONE, LoopType.ONE, LoopType.ALL})
    public @interface LoopType {
        int NONE = 0;
        int ONE = 1;
        int ALL = 2;
    }

    @IntDef({ShuffleType.ON, ShuffleType.OFF})
    public @interface ShuffleType {
        int OFF = 0;
        int ON = 1;
    }
}
