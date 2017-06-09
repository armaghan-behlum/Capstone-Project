package com.upaudio.armi.upaudio.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upaudio.armi.upaudio.R;
import com.upaudio.armi.upaudio.io.AudioFilesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PodcastListFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LoaderManager.LoaderCallbacks<List<File>>{

    /**
     * ID for file loader
     */
    private static final int FILE_LOADER = 0;

    @BindView(R.id.podcast_list)
    RecyclerView podcastListView;

    @BindView(R.id.empty_list_message)
    TextView emptyListMessageView;

    /**
     * Adapter for podcast files
     */
    private PodcastListAdapter podcastFilesListAdapter;

    public PodcastListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.PERMISSION_TO_WRITE_EXTERNAL_STORAGE);
        }

        podcastFilesListAdapter = new PodcastListAdapter(new ArrayList<File>());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FILE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_list, container, false);
        ButterKnife.bind(this, view);
        podcastListView.setAdapter(podcastFilesListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        podcastListView.setLayoutManager(layoutManager);
        podcastListView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        podcastListView.setAdapter(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MainActivity.PERMISSION_TO_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLoaderManager().restartLoader(FILE_LOADER, null, this);
                } else {
                    Timber.e("We do not have permissions to load the list");
                    emptyListMessageView.setVisibility(View.VISIBLE);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        return new PodcastFileAsyncTaskLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<File>> loader, List<File> data) {
        podcastFilesListAdapter.updateFiles(data);
        if (data.isEmpty()) {
            emptyListMessageView.setVisibility(View.VISIBLE);
        } else {
            emptyListMessageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<File>> loader) {
        // no-op
    }

    /**
     * Async Task Loader used to get podcast files
     */
    private static class PodcastFileAsyncTaskLoader extends AsyncTaskLoader<List<File>> {

        /**
         * Constructor
         *
         * @param context context for super class
         */
        PodcastFileAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public List<File> loadInBackground() {
            if (AudioFilesManager.isStorageAvailable()) {
                return AudioFilesManager.getPodcastFiles();
            } else {
               return new ArrayList<>();
            }

        }
    }
}
