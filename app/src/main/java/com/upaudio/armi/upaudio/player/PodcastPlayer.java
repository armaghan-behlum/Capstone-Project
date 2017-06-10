package com.upaudio.armi.upaudio.player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

/**
 * Wrapper for player library that also ensures singleton access
 */
public class PodcastPlayer {

    /**
     * Instance of PodcastPlayer that's used
     */
    private static PodcastPlayer instance;

    /**
     * Reference to player
     */
    private SimpleExoPlayer player;

    /**
     * data source factory
     */
    private DataSource.Factory dataSourceFactory;

    /**
     * Extractors factory
     */
    private ExtractorsFactory extractorsFactory;

    /**
     * Track selector
     */
    private TrackSelector trackSelector;

    /**
     * Current file playing
     */
    private String currentFile;

    /**
     * Current view used for playing
     */
    private SimpleExoPlayerView currentView;

    /**
     * Private constructor to ensure only one is made
     *
     * @param context context for exoplayer
     */
    private PodcastPlayer(Context context) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "UpAudio"));
        extractorsFactory = new DefaultExtractorsFactory();

    }

    /**
     * Returns singleton instance of PodcastPlayer
     *
     * @return PodcastPlayer instance
     */
    public static PodcastPlayer getInstance(Context context) {
        if (instance == null) {
            instance = new PodcastPlayer(context);

        }

        return instance;
    }

    /**
     * Starts a file for playback
     *
     * @param simpleExoPlayerView view to play in
     * @param fileToPlay location of file to be played
     */
    public void start(SimpleExoPlayerView simpleExoPlayerView, String fileToPlay) {
        if (fileToPlay.equals(currentFile)) {
            SimpleExoPlayerView.switchTargetView(player, currentView, simpleExoPlayerView);
            currentView = simpleExoPlayerView;
            return;
        }
        releasePlayer();

        player = ExoPlayerFactory.newSimpleInstance(simpleExoPlayerView.getContext(), trackSelector);
        MediaSource videoSource = new ExtractorMediaSource(Uri.fromFile(new File(fileToPlay)),
                dataSourceFactory, extractorsFactory, null, null);
        simpleExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.prepare(videoSource);
        currentView = simpleExoPlayerView;
        currentFile = fileToPlay;
    }

    /**
     * Gets current position in player
     *
     * @return current position
     */
    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    /**
     * Release player to free up resources
     */
    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
