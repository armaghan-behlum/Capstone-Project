package com.upaudio.armi.upaudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.upaudio.armi.upaudio.R;

import timber.log.Timber;

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
    public static final String ACTION_PLAY_FILE = "com.upaudio.armi.ui.ACTION_PLAY_FILE";

    /**
     * Reference to podcast list fragment
     */
    private PodcastListFragment podcastListFragment;

    /**
     * Reference to player fragment
     */
    private PlayerFragment playerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            onNewIntent(getIntent());
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            menu.removeItem(R.id.action_sign_in);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_in:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        if (!getResources().getBoolean(R.bool.twoPaneMode)) {
            if (podcastListFragment == null) {
                podcastListFragment = new PodcastListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, podcastListFragment).commit();
            }

            if (intent.hasExtra(EXTRA_FILE_NAME)) {
                if (playerFragment == null) {
                    playerFragment = new PlayerFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, playerFragment)
                            .addToBackStack(PlayerFragment.class.toString())
                            .commit();
                } else {
                    playerFragment.updateFile(intent.getStringExtra(EXTRA_FILE_NAME));
                }
            }
        } else {
           if (playerFragment == null) {
               playerFragment = (PlayerFragment) getSupportFragmentManager()
                       .findFragmentById(R.id.player_fragment);
           }
           playerFragment.updateFile(intent.getStringExtra(EXTRA_FILE_NAME));
        }
    }

    @Override
    public void onBackPressed() {
        int topOfStack = getSupportFragmentManager().getBackStackEntryCount() - 1;
        if (topOfStack > -1) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(topOfStack);
            if (entry.getName().equals(PlayerFragment.class.toString())) {
                playerFragment = null;
            }
        }
        super.onBackPressed();
    }
}
