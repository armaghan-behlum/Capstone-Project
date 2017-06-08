package com.upaudio.armi.upaudio.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.upaudio.armi.upaudio.R;
import com.upaudio.armi.upaudio.note.NotesDatabase;
import com.upaudio.armi.upaudio.note.UpAudioNote;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditorFragment extends Fragment implements ValueEventListener {

    @BindView(R.id.title_text_edit)
    EditText titleEditTextView;

    @BindView(R.id.note_text_edit)
    EditText messageEditTextView;

    @BindView(R.id.start_time_text)
    TextView startTimeTextView;

    @BindView(R.id.end_time_text)
    TextView endTimeTextView;

    @BindView(R.id.save_button)
    Button saveButton;

    /**
     * File name of note
     */
    private String fileName;

    /**
     * Tracks note id
     */
    private String noteId;

    /**
     * Reference to UpAudio on display
     */
    private UpAudioNote upAudioNote;

    /**
     * Tracks if changes to UpAudio were made
     */
    private boolean isDataChanged;

    public NoteEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        ButterKnife.bind(this, view);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataChanged) {
                    upAudioNote.setNoteName(titleEditTextView.getText().toString());
                    upAudioNote.setNote(messageEditTextView.getText().toString());
                    NotesDatabase.getInstance().updateUpAudio(upAudioNote, noteId);
                    saveButton.setEnabled(false);
                }
            }
        });
        titleEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                isDataChanged = true;
                saveButton.setEnabled(true);
            }
        });
        messageEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                isDataChanged = true;
                saveButton.setEnabled(true);
            }
        });
        updateNote(getActivity().getIntent().getStringExtra(NotesActivity.EXTRA_FILE),
                getActivity().getIntent().getStringExtra(NotesActivity.EXTRA_NOTE_ID));
        return view;
    }

    /**
     * Updates note ID in view
     *
     * @param fileName file name of note
     * @param noteId note id
     */
    public void updateNote(String fileName, String noteId) {
        if (this.noteId != null) {
            NotesDatabase.getInstance().unregisterNoteListener(this, this.fileName, this.noteId);
        }
        this.fileName = fileName;
        this.noteId = noteId;
        NotesDatabase.getInstance().registerNoteListener(this, fileName, noteId);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() == null) {
            return;
        }
        upAudioNote = UpAudioNote.getGson().fromJson(dataSnapshot.getValue().toString(), UpAudioNote.class);
        titleEditTextView.setText(upAudioNote.getNoteName());
        messageEditTextView.setText(upAudioNote.getNote());
        startTimeTextView.setText(String.valueOf(upAudioNote.getStartTime()));
        endTimeTextView.setText(String.valueOf(upAudioNote.getEndTime()));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
