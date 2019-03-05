package com.example.music_58.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.music_58.R;
import com.example.music_58.data.model.Track;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private List<Track> mTracks;
    private LayoutInflater mInflater;

    public TrackAdapter(Context context, List<Track> tracks) {
        mTracks = tracks;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.item_local_track, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setData(mTracks.get(i));
    }

    @Override
    public int getItemCount() {
        return (mTracks != null) ? mTracks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageTrack;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(Track track) {

        }
    }
}
