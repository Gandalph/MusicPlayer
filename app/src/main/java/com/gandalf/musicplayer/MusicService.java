package com.gandalf.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    IBinder mBinder = new MusicBinder();
    MediaPlayer mMusicPlayer = null;
    Handler mHandler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Music" + File.separator + "Ona.mp3";
        mMusicPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(path));
        
        return mBinder;
    }

    public class MusicBinder extends Binder {

        public Service getService() {
            return MusicService.this;
        }
    }

    public void playTrack() {
        Log.d(TAG, "play");
        if(mMusicPlayer != null)
            mMusicPlayer.start();
        mHandler.postDelayed(updateTime, 100);
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

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            int duration = mMusicPlayer.getCurrentPosition() / 60;
            Log.d(TAG, "Time: " + duration);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        if(mMusicPlayer != null) {
            mMusicPlayer.release();
            mMusicPlayer = null;
        }
        return super.onUnbind(intent);
    }
}
