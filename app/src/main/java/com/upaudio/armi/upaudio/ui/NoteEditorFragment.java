package com.upaudio.armi.upaudio.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upaudio.armi.upaudio.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditorFragment extends Fragment {

    @BindView(R.id.test_note)
    TextView textView;

    /**
     * Tracks note id
     */
    private String noteId;

    public NoteEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        ButterKnife.bind(this, view);
        textView.setText(getActivity().getIntent().getStringExtra(NotesActivity.EXTRA_NOTE_ID));
        return view;
    }

    /**
     * Updates note ID in view
     *
     * @param noteId note id
     */
    public void updateNote(String noteId) {
       textView.setText(noteId);
    }

}
