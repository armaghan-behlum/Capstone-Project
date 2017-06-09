package com.upaudio.armi.upaudio.note;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Date;

import timber.log.Timber;

/**
 * Wrapper for Firebase Realtime Database that simplifies calls
 */
public class NotesDatabase {

    /**
     * Time stamp used to track sync
     */
    private static final String TIME_STAMP = "TIME_STAMP";

    /**
     * Static instance of database
     */
    private static NotesDatabase instance;

    /**
     * Reference to database
     */
    private DatabaseReference userRef;

    private NotesDatabase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("user-notes").child(userId);
    }

    /**
     * Returns instance of database
     *
     * @return instance of database
     */
    public static NotesDatabase getInstance() {
        if (instance == null) {
            instance = new NotesDatabase();
        }
        return instance;
    }

    /**
     * Saves UpAudio note
     *
     * @param note UpAudio note
     * @return key of new saved item
     */
    public String saveUpAudio(UpAudioNote note) {
        DatabaseReference pushRef = userRef.child(getKeyFromFile(note.getFileName())).push();
        pushRef.setValue(UpAudioNote.getGson().toJson(note, UpAudioNote.class));
        return pushRef.getKey();
    }

    /**
     * Updates UpAudio note
     *
     * @param note note to be saved
     * @param noteId id of note
     */
    public void updateUpAudio(UpAudioNote note, String noteId) {
        String newNote = UpAudioNote.getGson().toJson(note, UpAudioNote.class);
        userRef.child(getKeyFromFile(note.getFileName())).child(noteId).setValue(newNote);
    }

    /**
     * Registers event listener for a list of notes associated with a file
     *
     * @param childEventListener child event listener
     * @param fileName file that is being tracked
     */
    public void registerListChangeListener(ChildEventListener childEventListener, String fileName) {
        userRef.child(getKeyFromFile(fileName)).addChildEventListener(childEventListener);
    }

    /**
     * Unregisters event listener for a list of notes associated with a file
     *
     * @param childEventListener child event listener
     * @param fileName file that was being tracked
     */
    public void unregisterListChangeListener(ChildEventListener childEventListener, String fileName) {
        userRef.child(getKeyFromFile(fileName)).removeEventListener(childEventListener);
    }

    /**
     * Registers listener for changes in a single note
     *
     * @param valueEventListener listener for events
     * @param fileName file name note is associated with
     * @param noteKey note being monitored
     */
    public void registerNoteListener(ValueEventListener valueEventListener, String fileName, String noteKey) {
        Timber.e("NotesDatabase#registerNoteListener:83 - file name - " + fileName);
        Timber.e("NotesDatabase#registerNoteListener:86 - note key - " + noteKey);
        userRef.child(getKeyFromFile(fileName)).child(noteKey).addValueEventListener(valueEventListener);
    }

    /**
     * Registers listener for changes in a single note
     *
     * @param valueEventListener listener for events
     * @param fileName file name note is associated with
     * @param noteKey note being monitored
     */
    public void unregisterNoteListener(ValueEventListener valueEventListener, String fileName, String noteKey) {
        userRef.child(getKeyFromFile(fileName)).child(noteKey).removeEventListener(valueEventListener);
    }

    /**
     * Converts a file name to a key that works for Firebase
     *
     * @param fileName file name
     * @return key that works with fire base
     */
    private String getKeyFromFile(String fileName) {
        File file = new File(fileName);
        return file.getName().replace(".", "-");
    }

    /**
     * Syncs Firebase database
     */
    public void sync() {
        userRef.child(TIME_STAMP).setValue(System.currentTimeMillis());
    }
}
