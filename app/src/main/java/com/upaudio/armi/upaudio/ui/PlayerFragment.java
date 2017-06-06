package com.upaudio.armi.upaudio.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.upaudio.armi.upaudio.R;
import com.upaudio.armi.upaudio.player.PodcastPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String fileToPlay = getArguments().getString(MainActivity.EXTRA_FILE_NAME, "");
            updateFile(fileToPlay);
        }
        return view;
    }

    /**
     * Updates file in the player
     *
     * @param fileToPlay file to start playing
     */
    public void updateFile(String fileToPlay) {
        PodcastPlayer.getInstance(getActivity().getApplicationContext()).start(playerView, fileToPlay);
    }

}
