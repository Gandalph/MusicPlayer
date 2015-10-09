package com.gandalf.musicplayer;

@SuppressWarnings("ALL")
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof Song) {
            Song song = (Song) o;
            if (song.mSongPath.compareTo(this.mSongPath) == 0) {
                return true;
            }
        }
        return false;
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
