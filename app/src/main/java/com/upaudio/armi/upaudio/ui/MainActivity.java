package com.upaudio.armi.upaudio.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.upaudio.armi.upaudio.R;

public class MainActivity extends AppCompatActivity {

    /**
     * Permission to write external storage
     */
    public static final int PERMISSION_TO_WRITE_EXTERNAL_STORAGE = 0;

    /**
     * Key to get file name in player
     */
    public static final String EXTRA_FILE_NAME = "extra_file_name";

    /**
     * Action to play a file
     */
    public static final String ACTION_PLAY_FILE = "action_play_file";

    /**
     * Reference to player fragment
     */
    private PlayerFragment playerFragment;

    /**
     * Broadcast receiver for player play requests
     */
    BroadcastReceiver playerBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!getResources().getBoolean(R.bool.twoPaneMode)) {
            if (savedInstanceState != null) {
                return;
            }

            PodcastListFragment podcastListFragment = new PodcastListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, podcastListFragment).commit();
        } else {
            playerFragment = (PlayerFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.player_fragment);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PLAY_FILE);
        playerBroadcastReceiver = new PlayerBroadcastReceiver();
        registerReceiver(playerBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(playerBroadcastReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    /**
     * Receives broadcasts for player play requests
     */
    class PlayerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(EXTRA_FILE_NAME)) {
                if (!getResources().getBoolean(R.bool.twoPaneMode)) {
                    playerFragment = new PlayerFragment();
                    playerFragment.setArguments(intent.getExtras());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, playerFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    playerFragment.updateFile(intent.getStringExtra(EXTRA_FILE_NAME));
                }
            }
        }
    }
}
