package com.upaudio.armi.upaudio.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upaudio.armi.upaudio.R;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);

        String file = getArguments().getString(MainActivity.EXTRA_FILE_NAME);
        Timber.e("PlayerFragment#onCreateView:30 - file: " + file);
        return view;
    }

    /**
     * Updates file in the player
     *
     * @param fileName file to update
     */
    public void updateFile(String fileName) {
        Timber.e("PlayerFragment#updateFile:40 - file: " + fileName);
    }
}
