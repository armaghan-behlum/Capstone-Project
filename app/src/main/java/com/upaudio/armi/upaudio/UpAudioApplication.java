package com.upaudio.armi.upaudio;

import android.app.Application;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.upaudio.armi.upaudio.jobdispatcher.NotesJobService;

import timber.log.Timber;

/**
 * UpAudio application class
 */
public class UpAudioApplication extends Application {

    /**
     * Firebase sync job
     */
    public static final String SYNC_JOB_ID = "sync_job_id";

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        firebaseJobDispatcher.schedule(getJob(firebaseJobDispatcher));
    }

    private Job getJob(FirebaseJobDispatcher dispatcher) {
        return dispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setService(NotesJobService.class)
                .setTag(SYNC_JOB_ID)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setTrigger(Trigger.executionWindow(3600, 86400)) // Sync after an hour but within next day
                .build();
    }
}
