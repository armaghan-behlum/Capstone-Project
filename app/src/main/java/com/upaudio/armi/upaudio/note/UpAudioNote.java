package com.upaudio.armi.upaudio.note;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * UpAudio note file
 */
public class UpAudioNote {

    /**
     * Reference to Gson
     */
    private static Gson gson;

    /**
     * Get Gson reference
     */
    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }

        return gson;
    }

    /**
     * Filename of UpAudio
     */
    private final String fileName;

    /**
     * Start time of UpAudio
     */
    private final long startTime;

    /**
     * End time of UpAudio
     */
    private final long endTime;

    /**
     * Option note that UpAudio can have
     */
    private String note;

    /**
     * Constructor
     *
     * @param fileName  file name
     * @param startTime start time of UpAudio
     * @param endTime   end time of UpAudio
     */
    public UpAudioNote(String fileName, long startTime, long endTime) {
        this(fileName, startTime, endTime, "");
    }

    /**
     * Constructor
     *
     * @param fileName  file name
     * @param startTime start time of UpAudio
     * @param endTime   end time of UpAudio
     * @param note      option note of UpAudio
     */
    public UpAudioNote(String fileName, long startTime, long endTime, String note) {
        this.fileName = fileName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.note = note;
    }

    public String getFileName() {
        return fileName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "UpAudioNote{" +
                "fileName='" + fileName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", note='" + note + '\'' +
                '}';
    }
}
