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
import com.example.music_58.data.model.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private List<Genre> mGenres;
    private GenreClickListener mListener;
    private LayoutInflater mInflater;

    public GenreAdapter(Context context, GenreClickListener listener, List<Genre> genres) {
        mInflater = LayoutInflater.from(context);
        mListener = listener;
        mGenres = genres;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.item_genres, viewGroup, false);
        return new ViewHolder(itemView, mListener);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setData(mGenres.get(position));
    }

    @Override
    public int getItemCount() {
        return (mGenres != null) ? mGenres.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mGenreImage;
        private TextView mTextGenreName;
        private GenreClickListener mListener;
        private Genre mGenre;

        public ViewHolder(View itemView, GenreClickListener listener) {
            super(itemView);
            mGenreImage = itemView.findViewById(R.id.image_genres);
            mTextGenreName = itemView.findViewById(R.id.text_genres_name);
            mListener = listener;
        }

        public void setData(Genre genre) {
            Glide.with(itemView.getContext())
                    .load(genre.getPhoto())
                    .centerCrop()
                    .placeholder(R.drawable.genre_all)
                    .into(mGenreImage);
            mTextGenreName.setText(genre.getName());
            itemView.setOnClickListener(this);
            mGenre = genre;
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(mGenre);
        }
    }

    public interface GenreClickListener {
        void onItemClick(Genre genre);
    }
}
