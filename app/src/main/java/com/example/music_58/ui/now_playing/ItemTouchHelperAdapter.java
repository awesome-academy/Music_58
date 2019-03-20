package com.example.music_58.ui.now_playing;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemMoved();
}
