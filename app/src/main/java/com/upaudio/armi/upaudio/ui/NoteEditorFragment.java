package com.upaudio.armi.upaudio.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upaudio.armi.upaudio.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditorFragment extends Fragment {


    public NoteEditorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_editor, container, false);
    }

}
