package com.gandalf.musicplayer;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListSongsFragment extends ListFragment {
//    private static final String TAG = "ListSongFragment";
    ArrayList<Song> mSongs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_activity_list_songs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list_songs, container, false);

        mSongs = new ArrayList<>(SongsLab.instance(getActivity()).getSongs());
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
}
