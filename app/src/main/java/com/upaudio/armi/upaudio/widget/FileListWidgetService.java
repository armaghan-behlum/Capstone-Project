package com.upaudio.armi.upaudio.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Provides RemoteView Factory for list
 */
public class FileListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FileListWidgetRemoteViewFactory(getApplicationContext());
    }
}
