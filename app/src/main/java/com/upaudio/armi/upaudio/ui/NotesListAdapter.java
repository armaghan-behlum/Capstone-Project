package com.upaudio.armi.upaudio.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.upaudio.armi.upaudio.R;
import com.upaudio.armi.upaudio.note.NotesDatabase;
import com.upaudio.armi.upaudio.note.UpAudioNote;

import java.util.ArrayList;
import java.util.List;

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
            NotesDatabase.getInstance().unregisterListChangeListener(this, this.fileName);
        }
        this.fileName = fileName;
        upAudioNoteList.clear();
        keyList.clear();
        notifyDataSetChanged();
        NotesDatabase.getInstance().registerListChangeListener(this, fileName);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_list_item, viewGroup, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int position) {
        UpAudioNote upAudioNote = upAudioNoteList.get(position);
        Context context = noteViewHolder.itemView.getContext();
        noteViewHolder.titleTextView.setText(upAudioNote.getNoteName());
        noteViewHolder.startTextView.setText(context.getString(R.string.start_time_and_val, upAudioNote.getStartTime()));
        noteViewHolder.endTextView.setText(context.getString(R.string.end_time_and_val, upAudioNote.getEndTime()));
        noteViewHolder.messageTextView.setText(upAudioNote.getNote());
    }

    @Override
    public int getItemCount() {
        return upAudioNoteList.size();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String key) {
        UpAudioNote upAudioNote = UpAudioNote.getGson().fromJson(dataSnapshot.getValue().toString(), UpAudioNote.class);
        keyList.add(dataSnapshot.getKey());
        upAudioNoteList.add(upAudioNote);
        notifyItemInserted(upAudioNoteList.size() - 1);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        int position = keyList.indexOf(key);
        if (position == -1) {
            return;
        }
        UpAudioNote upAudioNote = UpAudioNote.getGson().fromJson(dataSnapshot.getValue().toString(), UpAudioNote.class);
        upAudioNoteList.remove(position);
        upAudioNoteList.add(position, upAudioNote);
        notifyItemChanged(position);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        int position = keyList.indexOf(key);
        if (position == -1) {
            return;
        }

        keyList.remove(key);
        upAudioNoteList.remove(position);
        notifyItemRemoved(position);
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

        View itemView;
        TextView titleTextView;
        TextView startTextView;
        TextView endTextView;
        TextView messageTextView;

        NoteViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            startTextView = (TextView) itemView.findViewById(R.id.start_time_text);
            endTextView = (TextView) itemView.findViewById(R.id.end_time_text);
            messageTextView = (TextView) itemView.findViewById(R.id.message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(NotesActivity.ACTION_EDIT_NOTE);
            intent.putExtra(NotesActivity.EXTRA_FILE, fileName);
            intent.putExtra(NotesActivity.EXTRA_NOTE_ID, keyList.get(getAdapterPosition()));
            v.getContext().startActivity(intent);
        }
    }
}
