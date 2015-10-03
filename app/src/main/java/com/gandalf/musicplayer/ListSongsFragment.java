package com.gandalf.musicplayer;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ListSongsFragment extends ListFragment {
    private static final String TAG = "ListSongFragment";
    ArrayList<Song> mSongs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list_songs, container, false);

        mSongs = new ArrayList<>(getSongs());
        SongsAdapter mSongAdapter = new SongsAdapter(mSongs);
        setListAdapter(mSongAdapter);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(getActivity(), MusicPlayerActivity.class);
        i.putExtra(MusicPlayerFragment.EXTRA_SONG_PATH, ((Song)l.getAdapter().getItem(position)).getSongPath());
        startActivity(i);
    }

    private ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        File songsPath;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //songsPath = (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Music")).listFiles();
            songsPath = new File("/mnt/sdcard2/Music/09-Prica_o_ljubavi_obicno_ugnjavi");

            for(String f : songsPath.list())
                Log.d(TAG, f);

            for(File f : songsPath.listFiles()) {
                Log.d(TAG, f.getName());
                Mp3Info m = new Mp3Info().setMp3Path(f.getAbsolutePath());
                songs.add(new Song(m.getAlbum(), m.getTitle(), m.getAuthor(), m.getYear(), f.getAbsolutePath()));
            }
        }
        return songs;
    }

    public class SongsAdapter extends ArrayAdapter<Song> {

        public SongsAdapter(ArrayList<Song> objects) {
            super(getActivity(), android.R.layout.simple_list_item_2, objects);
        }

        @Override
        public Song getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getCount() {
            return mSongs.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);

            Song song = getItem(position);

            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            text1.setText(song.getTitle());
            TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
            text2.setText(song.getAuthor());

            return convertView;
        }
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
                int l = (int)f.length();
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
            if(mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(3, 32);
            return "Unknown";
        }

        public String getAuthor() {
            if(mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(33, 62);
            return "Unknown";
        }

        public String getAlbum() {
            if(mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(63, 91);
            return "Unknown";
        }

        public String getYear() {
            if(mMetadata.substring(0, 3).equals("TAG"))
                return mMetadata.substring(93, 97);
            return "Unknown";
        }


    }
}
