package com.upaudio.armi.upaudio;

import android.app.Application;

import timber.log.Timber;

/**
 * UpAudio application class
 */
public class UpAudioApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
