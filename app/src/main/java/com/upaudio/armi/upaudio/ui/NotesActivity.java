package com.upaudio.armi.upaudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.upaudio.armi.upaudio.R;

public class NotesActivity extends AppCompatActivity {

    /**
     * Used to package file name
     */
    public static final String EXTRA_FILE = "extra_file";

    /**
     * Used to package note id
     */
    public static final String EXTRA_NOTE_ID = "extra_note_id";

    /**
     * Action sent to launch note editor
     */
    public static final String ACTION_EDIT_NOTE = "com.upaudio.armi.upaudio.ui.ACTION_EDIT_NOTE";

    /**
     * Reference to note list fragment
     */
    private NoteListFragment noteListFragment;

    /**
     * Reference to note editor fragmen
     */
    private NoteEditorFragment noteEditorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.root_view), R.string.sorry_not_signed_in, Snackbar.LENGTH_LONG);
            snackbar.show();
            finish();
        }
        if (savedInstanceState == null) {
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        if (!getResources().getBoolean(R.bool.twoPaneMode)) {
            if (noteListFragment == null) {
                noteListFragment = new NoteListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, noteListFragment).commit();
            }

            if (getIntent().hasExtra(EXTRA_NOTE_ID)) {
                if (noteEditorFragment == null) {
                    noteEditorFragment = new NoteEditorFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, noteEditorFragment)
                            .addToBackStack(NoteEditorFragment.class.toString()).commit();
                } else {
                    noteEditorFragment.updateNote(getIntent().getStringExtra(NotesActivity.EXTRA_FILE),
                            getIntent().getStringExtra(EXTRA_NOTE_ID));
                }
            }
        } else {
            if (noteEditorFragment == null) {
                noteEditorFragment= (NoteEditorFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.note_editor_fragment);
            }
            if (noteListFragment == null) {
                 noteListFragment = (NoteListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.note_list_fragment);
            }

            noteEditorFragment.updateNote(getIntent().getStringExtra(EXTRA_FILE),
                    getIntent().getStringExtra(EXTRA_NOTE_ID));
        }
    }

    @Override
    public void onBackPressed() {
        int topOfStack = getSupportFragmentManager().getBackStackEntryCount() - 1;
        if (topOfStack > -1) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(topOfStack);
            if (entry.getName().equals(NoteEditorFragment.class.toString())) {
                noteEditorFragment = null;
            }
        }
        super.onBackPressed();
    }
}
