package com.gandalf.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    IBinder mBinder = new MusicBinder();
    MediaPlayer mMusicPlayer = null;
    Handler mHandler = new Handler();
    String mCurrentTrack = null;
    MusicPlayerFragment mMusicPlayerFragment;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mMusicPlayer = new MediaPlayer();

        return mBinder;
    }

    public class MusicBinder extends Binder {

        public Service getService() {
            return MusicService.this;
        }
        public void setFragment(MusicPlayerFragment fragment) {
            mMusicPlayerFragment = fragment;
        }
    }

    public void playTrack() {
        Log.d(TAG, "play");
        if(mMusicPlayer != null) {
            mMusicPlayer.start();
            setUpAndStartSeekBar();
        }
    }

    public void pauseTrack() {
        Log.d(TAG, "pause");
        if(mMusicPlayer != null && mMusicPlayer.isPlaying())
            mMusicPlayer.pause();
        mHandler.removeCallbacks(updateTime);
    }

    public void nextTrack() {
        Log.d(TAG, "next");
    }

    public void previousTrack() {
        Log.d(TAG, "previous");
    }

    public boolean isPlaying() {
        return mMusicPlayer.isPlaying();
    }

    public void setTrack(String track) {
        try {
            if(!track.equals(mCurrentTrack)) {
                Log.d(TAG, "setTrack if");
                mCurrentTrack = track;
                mMusicPlayer.reset();
                mMusicPlayer.setDataSource(mCurrentTrack);
                mMusicPlayer.prepare();
            }
            else {
                Log.d(TAG, "setTrack else");
                setUpAndStartSeekBar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setUpAndStartSeekBar() {
        Log.d(TAG, "duration: " + mMusicPlayer.getDuration() / 60);
        mMusicPlayerFragment.setMaxSeekBar(mMusicPlayer.getDuration() / 60);
        mHandler.postDelayed(updateTime, 100);
    }

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            int duration = mMusicPlayer.getCurrentPosition() / 60;
            //Log.d(TAG, "current position: " + duration);
            mMusicPlayerFragment.updateSeekBar(duration);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMusicPlayer != null) {
            mHandler.removeCallbacks(updateTime);
            mMusicPlayer.release();
            mMusicPlayer = null;
        }
    }
}
