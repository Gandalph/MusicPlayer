package com.gandalf.musicplayer;

import android.app.Fragment;

public class ListSongsActivity extends SingleFragmentActivity {

    @Override
    Fragment createFragment() {
        return new ListSongsFragment();
    }
}
