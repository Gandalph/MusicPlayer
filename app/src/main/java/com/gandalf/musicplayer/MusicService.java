package com.gandalf.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    IBinder mBinder = new MusicBinder();
    MediaPlayer mMusicPlayer = null;
    Handler mHandler = new Handler();
    String mCurrentTrack;
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
            mMusicPlayerFragment.setMaxSeekBar(mMusicPlayer.getDuration() / 60);
            mHandler.postDelayed(updateTime, 100);
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
        mCurrentTrack = track;
        try {
            mMusicPlayer.setDataSource(track);
            mMusicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            int duration = mMusicPlayer.getCurrentPosition() / 60;
            Log.d(TAG, "Time: " + duration);
            mMusicPlayerFragment.updateSeekBar(duration);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        if(mMusicPlayer != null) {
            mHandler.removeCallbacks(updateTime);
            mMusicPlayer.release();
            mMusicPlayer = null;
        }
        return super.onUnbind(intent);
    }
}
