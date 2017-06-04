package com.upaudio.armi.upaudio.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import timber.log.Timber;

/**
 * Adapter for podcast recycler view
 */
class PodcastListAdapter extends RecyclerView.Adapter<PodcastListAdapter.PodcastListViewHolder> {

    /**
     * List of podcast files for view
     */
    private List<File> podcastFiles;

    /**
     * Constructor
     *
     * @param files list of podcast files
     */
    PodcastListAdapter(List<File> files) {
        podcastFiles = files;
    }

    @Override
    public PodcastListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        return new PodcastListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PodcastListViewHolder holder, int position) {
        holder.fileNameTextView.setText(podcastFiles.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return podcastFiles.size();
    }

    /**
     * Updates list of files
     *
     * @param newFiles new files for view
     */
    void updateFiles(List<File> newFiles) {
        podcastFiles = newFiles;
        Timber.e("list updated to - " + podcastFiles);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for podcast items
     */
    static class PodcastListViewHolder extends RecyclerView.ViewHolder {

        /**
         * TextView that holds file name
         */
        TextView fileNameTextView;

        /**
         * Constructor
         *
         * @param itemView itemView held
         */
        PodcastListViewHolder(View itemView) {
            super(itemView);
            fileNameTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
