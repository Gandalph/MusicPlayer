package com.gandalf.musicplayer;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MusicPlayerFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
//    private static final String TAG = "MusicPlayerFragment";
    public static final String EXTRA_SONG_PATH = "com.gandalf.musicplayer.MusicPlayerFragment";
    MusicService mMusicService;
    MusicService.MusicBinder mBinder;
    boolean isBound = false;
    String mSongPath;
    SeekBar mTimeElapsedSeekBar;
    TextView mTimeElapsed;
    TextView mCurrentTrack;
    String mCurrentSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isBound) {
            mSongPath = getArguments().getString(EXTRA_SONG_PATH);
            setCurrentTrackName(mSongPath);
            Toast.makeText(getActivity(), mSongPath, Toast.LENGTH_LONG).show();
            bindService();
        }
    }

    private void bindService() {
        Intent i = new Intent(getActivity(), MusicService.class);
        getActivity().startService(i);
        getActivity().bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(boolean stop) {
        if(isBound) {
            getActivity().unbindService(mServiceConnection);
            isBound = false;
            if(stop) {
                getActivity().stopService(new Intent(getActivity(), MusicService.class));
            }
        }
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

        mTimeElapsedSeekBar = (SeekBar) v.findViewById(R.id.time_elapsed_seekBar);
        mTimeElapsedSeekBar.setOnSeekBarChangeListener(this);

        mCurrentTrack = (TextView) v.findViewById(R.id.current_track);

        mTimeElapsed = (TextView) v.findViewById(R.id.time_elapsed);

        return v;
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MusicService.MusicBinder) service;
            mMusicService = (MusicService) mBinder.getService();
            isBound = true;
            mMusicService.setTrack(mSongPath);
            mBinder.setFragment(MusicPlayerFragment.this);
            mMusicService.setUpAndStartSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    // OnClickListener
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
    //-------------------------------------------------

    public void setMaxSeekBar(int duration) {
        if(mTimeElapsedSeekBar.getMax() != duration) {
            mTimeElapsedSeekBar.setMax(duration);
        }

    }

    public void updateSeekBar(long currentTime) {
        int t = mTimeElapsedSeekBar.getMax() < (int) (currentTime / 60) ?
                mTimeElapsedSeekBar.getMax() : (int) (currentTime / 60);
        mTimeElapsedSeekBar.setProgress(t);
        long mSecond = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60;
        long mMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime);

        String s = (mMinutes < 10 ? "0"+mMinutes : mMinutes+"");
        s += ":";
        s += (mSecond < 10 ? "0"+mSecond : mSecond+"");
        mTimeElapsed.setText(s);
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindService(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(true);
    }

    public static MusicPlayerFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SONG_PATH, path);
        MusicPlayerFragment instance = new MusicPlayerFragment();
        instance.setArguments(args);
        return instance;
    }

    public void setCurrentTrackName(String currentSong) {
        String[] splitSong;
        if (currentSong != null) {
            mSongPath = currentSong;
            splitSong = mSongPath.split("/");
            mCurrentSong = splitSong[splitSong.length-1];
            mCurrentTrack.setText(mCurrentSong);
        }
    }

    // OnSeekBarChangeListener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMusicService.seekTo(seekBar.getProgress() * 60);
    }
    //-----------------------------------------------------
}

