package com.gandalf.musicplayer;

import android.app.Fragment;

public class MusicPlayerActivity extends SingleFragmentActivity {

    @Override
    Fragment createFragment() {
        String path = getIntent().getStringExtra(MusicPlayerFragment.EXTRA_SONG_PATH);
        return MusicPlayerFragment.newInstance(path);
    }
}
