package com.example.music_58.data.model;

import android.support.annotation.IntDef;

@IntDef({
        FragmentName.HOME,
        FragmentName.FAVORITE,
        FragmentName.LOCAL
})
public @interface FragmentName {
    int HOME = 0;
    int FAVORITE = 1;
    int LOCAL = 2;
}
