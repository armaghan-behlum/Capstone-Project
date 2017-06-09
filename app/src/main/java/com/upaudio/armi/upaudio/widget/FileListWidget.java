package com.upaudio.armi.upaudio.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.upaudio.armi.upaudio.R;
import com.upaudio.armi.upaudio.ui.MainActivity;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class FileListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_list_widget);

        Intent intent = new Intent(context, FileListWidgetService.class);
        views.setRemoteAdapter(R.id.file_list, intent);
        views.setEmptyView(R.id.file_list, R.id.widget_list_empty_view);

        Intent playItemIntent = new Intent();
        playItemIntent.setAction(MainActivity.ACTION_PLAY_FILE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, playItemIntent, 0);
        views.setPendingIntentTemplate(R.id.file_list, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

