package com.upaudio.armi.upaudio.io;

import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * Used to find audio files to be used
 */
public class AudioFilesManager {

    /**
     * Audio MIME type
     */
    private static final String AUDIO_MIME_TYPE = "audio";

    /**
     * Filter to find only audio files
     */
    private static final FileFilter audioFileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            if (extension == null) {
                return false;
            }
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            return mimeType != null && mimeType.contains(AUDIO_MIME_TYPE);
        }
    };

    /**
     * Returns true if storage is available
     *
     * @return true if storage is available
     */
    public static boolean isStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Returns podcast files
     *
     * @return list of podcast files
     */
    public static List<File> getPodcastFiles() {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);

        if (!directory.mkdirs() && directory.listFiles() != null) {
            return findAudioFiles(directory);
        } else {
            Timber.e("AudFilesManager.getPodcastFiles: no files found in directory");
            return new ArrayList<>();
        }
    }

    /**
     * Returns the absolute path of the podcast file
     *
     * @param fileName file name
     * @return absolute name
     */
    public static String getPodcastFilePath(String fileName) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).toString()
                + "/" + fileName;
    }

    /**
     * Finds audio files in a given directory
     *
     * @param directory directory to search
     * @return list of podcast files
     */
    private static List<File> findAudioFiles(File directory) {
        return Arrays.asList(directory.listFiles(audioFileFilter));
    }

}
