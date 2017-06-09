package com.upaudio.armi.upaudio.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.upaudio.armi.upaudio.io.AudioFilesManager;
import com.upaudio.armi.upaudio.ui.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Provides remote views for widget
 */
public class FileListWidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    /**
     * List of podcast files for view
     */
    private final List<File> podcastFiles = new ArrayList<>();

    /**
     * Context used for getting view
     */
    private final Context context;

    public FileListWidgetRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        podcastFiles.clear();
        podcastFiles.addAll(AudioFilesManager.getPodcastFiles());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return podcastFiles.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        File file = podcastFiles.get(position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), android.R.layout.simple_expandable_list_item_1);
        remoteViews.setTextViewText(android.R.id.text1, file.getName());

        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_FILE_NAME, file.getName());
        remoteViews.setOnClickFillInIntent(android.R.id.text1, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
