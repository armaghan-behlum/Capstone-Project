package com.upaudio.armi.upaudio.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.upaudio.armi.upaudio.note.NotesDatabase;
import com.upaudio.armi.upaudio.note.UpAudioNote;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Notes list adapter
 */
class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> implements ChildEventListener {

    /**
     * File being tracked by notes
     */
    private String fileName;

    /**
     * Internal tracking of notes
     */
    private List<UpAudioNote> upAudioNoteList = new ArrayList<>();

    /**
     * Lists mirroring upAudioNoteList but with keys into database
     */
    private List<String> keyList = new ArrayList<>();

    /**
     * Sets file to be used by notes list
     *
     * @param fileName file name
     */
    void updateFileName(String fileName) {
        if (this.fileName != null) {
            NotesDatabase.getInstance().unregisterChildEventListener(this, this.fileName);
        }
        this.fileName = fileName;
        upAudioNoteList.clear();
        keyList.clear();
        notifyDataSetChanged();
        NotesDatabase.getInstance().registerChildEventListener(this, fileName);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int position) {
        noteViewHolder.noteTextView.setText(upAudioNoteList.get(position).getNoteName());
    }

    @Override
    public int getItemCount() {
        return upAudioNoteList.size();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String key) {
        keyList.add(key);
        UpAudioNote upAudioNote = UpAudioNote.getGson().fromJson(dataSnapshot.getValue().toString(), UpAudioNote.class);
        upAudioNoteList.add(upAudioNote);
        notifyItemInserted(upAudioNoteList.size() - 1);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    /**
     * ViewHolder for Notes info
     */
    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * TextView that holds file name
         */
        TextView noteTextView;

        NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = (TextView) itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Timber.e("NoteViewHolder#onClick:121 - on click set");
            intent.setAction(NotesActivity.ACTION_EDIT_NOTE);
            intent.putExtra(NotesActivity.EXTRA_FILE, fileName);
            intent.putExtra(NotesActivity.EXTRA_NOTE_ID, keyList.get(getAdapterPosition()));
            v.getContext().startActivity(intent);
        }
    }
}
