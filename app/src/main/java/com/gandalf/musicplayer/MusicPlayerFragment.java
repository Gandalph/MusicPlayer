package com.gandalf.musicplayer;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MusicPlayerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MusicPlayerFragment";
    public static final String EXTRA_SONG_PATH = "com.gandalf.musicplayer.MusicPlayerFragment";
    MusicService mMusicService;
    MusicService.MusicBinder mBinder;
    boolean isBound = false;
    String mSongPath;
    SeekBar mTimeElapsed;

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Bound: " + isBound);
        if(!isBound) {
            Log.d(TAG, "binding service");
            mSongPath = getArguments().getString(EXTRA_SONG_PATH);
            Toast.makeText(getActivity(), mSongPath, Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(i, mServiceConection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music_player, container, false);

        Button mPreviousTrack = (Button) v.findViewById(R.id.previous_track);
        mPreviousTrack.setOnClickListener(this);
        Button mPause = (Button) v.findViewById(R.id.pause);
        mPause.setOnClickListener(this);
        Button mPlay = (Button) v.findViewById(R.id.play);
        mPlay.setOnClickListener(this);
        Button mNextTrack = (Button) v.findViewById(R.id.next_track);
        mNextTrack.setOnClickListener(this);

        mTimeElapsed = (SeekBar) v.findViewById(R.id.time_elapsed_seekBar);
        mTimeElapsed.setClickable(false);

        return v;
    }

    ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MusicService.MusicBinder) service;
            mMusicService = (MusicService) mBinder.getService();
            isBound = true;
            mMusicService.setTrack(mSongPath);
            mBinder.setFragment(MusicPlayerFragment.this);
            Log.d(TAG, "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            mMusicService = null;
            mBinder = null;
            Log.d(TAG, "disconnected");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_track:
                mMusicService.previousTrack();
                break;
            case R.id.pause:
                mMusicService.pauseTrack();
                break;
            case R.id.play:
                mMusicService.playTrack();
                break;
            case R.id.next_track:
                mMusicService.nextTrack();
                break;
        }
    }

    public void setMaxSeekBar(int duration) {
        mTimeElapsed.setMax(duration);
    }

    public void updateSeekBar(int currentTime) {
        mTimeElapsed.setProgress(mTimeElapsed.getMax() < currentTime ? mTimeElapsed.getMax() : currentTime);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Service is playing: " + mMusicService.isPlaying());
        Log.d(TAG, "isBound: " + isBound);
        if(isBound && !mMusicService.isPlaying()) {
            getActivity().unbindService(mServiceConection);
        }
    }

    public static MusicPlayerFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SONG_PATH, path);
        MusicPlayerFragment instance = new MusicPlayerFragment();
        instance.setArguments(args);
        return instance;
    }
}

