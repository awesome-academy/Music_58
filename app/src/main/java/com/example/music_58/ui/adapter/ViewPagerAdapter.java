package com.example.music_58.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.music_58.data.model.FragmentName;
import com.example.music_58.ui.favorite.FavoriteFragment;
import com.example.music_58.ui.home.HomeFragment;
import com.example.music_58.ui.local.LocalMusicFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int SCREEN_COUNT = 3;

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case FragmentName.HOME:
                return HomeFragment.newInstance();
            case FragmentName.FAVORITE:
                return FavoriteFragment.newInstance();
            case FragmentName.LOCAL:
                return LocalMusicFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return SCREEN_COUNT;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
}
