package com.upaudio.armi.upaudio.jobdispatcher;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.upaudio.armi.upaudio.note.NotesDatabase;

/**
 * Job used to sync notes
 */
public class NotesJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        NotesDatabase.getInstance().sync();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
