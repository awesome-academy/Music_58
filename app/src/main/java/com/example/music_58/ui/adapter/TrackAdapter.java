package com.example.music_58.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
        View itemView = mInflater.inflate(R.layout.item_detail_genre, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setData(mTracks.get(i));
    }

    public void addTracks(List<Track> tracks) {
        if (tracks != null) {
            mTracks.addAll(tracks);
            notifyDataSetChanged();
        }
    }

    public void updateTracks(List<Track> tracks) {
        if (tracks != null) {
            mTracks.clear();
            mTracks.addAll(tracks);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return (mTracks != null) ? mTracks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageGenreDetail;
        private TextView mTextNameArtist;
        private TextView mTextSongName;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageGenreDetail = itemView.findViewById(R.id.image_detail_genre);
            mTextNameArtist = itemView.findViewById(R.id.text_artist_name);
            mTextSongName = itemView.findViewById(R.id.text_track_name);
        }

        public void setData(Track track) {
            Glide.with(itemView.getContext()).load(R.drawable.genre_classical)
                    .apply(RequestOptions.circleCropTransform()).into(mImageGenreDetail);
            mTextNameArtist.setText(track.getArtist());
            mTextSongName.setText(track.getTitle());
        }
    }
}
