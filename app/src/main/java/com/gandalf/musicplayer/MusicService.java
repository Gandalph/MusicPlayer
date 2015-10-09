package com.gandalf.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

public class MusicService extends Service {
//    private static final String TAG = "MusicService";
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
        if(mMusicPlayer != null) {
            mMusicPlayer.start();
        }
    }

    public void pauseTrack() {
        if(mMusicPlayer != null && mMusicPlayer.isPlaying())
            mMusicPlayer.pause();
        mHandler.removeCallbacks(updateTime);
    }

    private void nextPrevious(String trackPath) {
        setTrack(trackPath);
        playTrack();
        mMusicPlayerFragment.setCurrentTrackName(trackPath);
    }

    public void nextTrack() {
        String nextTrack = SongsLab.instance(getBaseContext()).getNextTrack(mCurrentTrack);
        if(nextTrack != null) {
            nextPrevious(nextTrack);
        }
    }

    public void previousTrack() {
        String previousTrack = SongsLab.instance(getBaseContext()).getPreviousTrack(mCurrentTrack);
        if(previousTrack != null) {
            nextPrevious(previousTrack);
        }
    }

    public void setTrack(String track) {
        try {
            if(!track.equals(mCurrentTrack)) {
                mCurrentTrack = track;
                mMusicPlayer.reset();
                mMusicPlayer.setDataSource(mCurrentTrack);
                mMusicPlayer.prepare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seekTo(int msec) {
        mMusicPlayer.seekTo(msec);
    }

    public void setUpAndStartSeekBar() {
        mMusicPlayerFragment.setMaxSeekBar(mMusicPlayer.getDuration() / 60);
        mHandler.postDelayed(updateTime, 1000);
    }

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            long duration = mMusicPlayer.getCurrentPosition();
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
