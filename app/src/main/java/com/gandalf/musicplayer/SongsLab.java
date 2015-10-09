package com.gandalf.musicplayer;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class SongsLab {
    public static SongsLab sSongsLab;
    private Context mContext;

    private ArrayList<Song> mSongs;

    public String getPreviousTrack(String current) {
        int i = mSongs.indexOf(new Song(null, null, null, null, current));
        if( (i > 0) && (i < mSongs.size()) ) {
            return mSongs.get(i-1).getSongPath();
        }
        return null;
    }

    public String getNextTrack(String current) {
        int i = mSongs.indexOf(new Song(null, null, null, null, current));
        if( (i >= 0) && (i < mSongs.size() - 1) ) {
            return mSongs.get(i+1).getSongPath();
        }
        return null;
    }

    public ArrayList<Song> getSongs() {
        return mSongs;
    }

    private SongsLab(Context c) {
        mContext = c;
        mSongs = listSongs();
    }

    public static SongsLab instance(Context c) {
        if(sSongsLab == null) {
            sSongsLab = new SongsLab(c);
            return sSongsLab;
        }
        return sSongsLab;
    }

    private ArrayList<Song> listSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        File songsPath;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            songsPath = (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Music"));
//            songsPath = new File("/mnt/sdcard2/Music/09-Prica_o_ljubavi_obicno_ugnjavi");

            for(File f : songsPath.listFiles()) {
                Mp3Info m = new Mp3Info().setMp3Path(f.getAbsolutePath());
                songs.add(new Song(m.getAlbum(), m.getTitle(), m.getAuthor(), m.getYear(), f.getAbsolutePath()));
            }
        }
        return songs;
    }

    public class Mp3Info {
        String mMp3Path;
        String mMetadata;

        public Mp3Info() {

        }

        public Mp3Info setMp3Path(String path) {
            try {
                mMp3Path = path;
                File f = new File(path);
                FileInputStream fileInputStream = new FileInputStream(f);
                int l = (int) f.length();
                fileInputStream.skip(l - 128);
                byte[] last128 = new byte[128];
                fileInputStream.read(last128);
                mMetadata = new String(last128);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Mp3Info.this;
        }

        public String getTitle() {
            if (mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(3, 32);
            return "Unknown";
        }

        public String getAuthor() {
            if (mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(33, 62);
            return "Unknown";
        }

        public String getAlbum() {
            if (mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(63, 91);
            return "Unknown";
        }

        public String getYear() {
            if (mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(93, 97);
            return "Unknown";
        }
    }
}
