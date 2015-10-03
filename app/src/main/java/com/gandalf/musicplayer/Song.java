package com.gandalf.musicplayer;

public class Song {
    private String mTitle;
    private String mAuthor;
    private String mAlbum;
    private String mYear;
    private String mSongPath;

    public Song(String album, String title, String author, String year, String path) {
        mAlbum = album;
        mTitle = title;
        mAuthor = author;
        mYear = year;
        mSongPath = path;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getYear() {
        return mYear;
    }

    public String getSongPath() {
        return mSongPath;
    }
}
