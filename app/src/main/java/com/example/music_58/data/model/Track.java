package com.example.music_58.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
    private int mId;
    private String mTitle;
    private int mDuration;
    private String mGenre;
    private String mDescription;
    private String mStreamUrl;
    private String mDownloadUrl;
    private boolean mIsDownloadable;
    private String mArtist;
    private String mAlbumTitle;
    private String mArtworkUrl;
    private boolean mIsFavorite;

    public Track(int id, String title, String artist) {
        mId = id;
        mTitle = title;
        mArtist = artist;
    }

    protected Track(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mDuration = in.readInt();
        mGenre = in.readString();
        mDescription = in.readString();
        mStreamUrl = in.readString();
        mDownloadUrl = in.readString();
        mIsDownloadable = in.readByte() != 0;
        mArtist = in.readString();
        mAlbumTitle = in.readString();
        mArtworkUrl = in.readString();
        mIsFavorite = in.readByte() != 0;
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public boolean isDownloadable() {
        return mIsDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        mIsDownloadable = downloadable;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getAlbumTitle() {
        return mAlbumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        mAlbumTitle = albumTitle;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeInt(mDuration);
        dest.writeString(mGenre);
        dest.writeString(mDescription);
        dest.writeString(mStreamUrl);
        dest.writeString(mDownloadUrl);
        dest.writeByte((byte) (mIsDownloadable ? 1 : 0));
        dest.writeString(mArtist);
        dest.writeString(mAlbumTitle);
        dest.writeString(mArtworkUrl);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));
    }
}
