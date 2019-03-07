package com.example.music_58.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_58.R;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private static final String[] GENRES_NAME = {"ALL", "AUDIO", "ALTERNATIVEROCK", "AMBIENT",
            "CLASSICAL", "COUNTRY"};
    private static final int[] GENRES_IMAGE = {
            R.drawable.genre_all,
            R.drawable.genre_audio,
            R.drawable.genre_alternative_rock,
            R.drawable.genre_ambient,
            R.drawable.genre_classical,
            R.drawable.genre_country
    };
    private GenreClickListener mListener;
    private LayoutInflater mInflater;

    public GenreAdapter(Context context, GenreClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.item_genres, viewGroup, false);
        return new ViewHolder(itemView, mListener);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return GENRES_NAME.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mGenreImage;
        private TextView mTextGenreName;
        private GenreClickListener mListener;

        public ViewHolder(View itemView, GenreClickListener listener) {
            super(itemView);
            mGenreImage = itemView.findViewById(R.id.image_genres);
            mTextGenreName = itemView.findViewById(R.id.text_genres_name);
            mListener = listener;
        }

        public void setData(int position) {
            Glide.with(itemView.getContext())
                    .load(GENRES_IMAGE[position])
                    .centerCrop()
                    .placeholder(R.drawable.genre_all)
                    .into(mGenreImage);
            mTextGenreName.setText(GENRES_NAME[position]);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(this.getLayoutPosition());
        }
    }

    public interface GenreClickListener {
        void onItemClick(int position);
    }
}
