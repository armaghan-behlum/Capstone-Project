package com.upaudio.armi.upaudio.note;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

/**
 * Wrapper for Firebase Realtime Database that simplifies calls
 */
public class NotesDatabase {

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
     */
    public void saveUpAudio(UpAudioNote note) {
        DatabaseReference pushRef = userRef.child(getKeyFromFile(note.getFileName())).push();
        pushRef.setValue(UpAudioNote.getGson().toJson(note, UpAudioNote.class));
    }

    /**
     * Registers event listener for a file
     *
     * @param childEventListener child event listener
     * @param fileName file that is being tracked
     */
    public void registerChildEventListener(ChildEventListener childEventListener, String fileName) {
        userRef.child(getKeyFromFile(fileName)).addChildEventListener(childEventListener);
    }

    /**
     * Unregisters event listener for a file
     *
     * @param childEventListener child event listener
     * @param fileName file that was being tracked
     */
    public void unregisterChildEventListener(ChildEventListener childEventListener, String fileName) {
        userRef.child(getKeyFromFile(fileName)).removeEventListener(childEventListener);
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
}
