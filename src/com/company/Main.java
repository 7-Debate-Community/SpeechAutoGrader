package com.company;


import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // directory where all audio file stored
    public static final String FOLDER_PATH = "C:\\Users\\steve\\Desktop\\test";
    // lower bound for audio length
    public static final int LOWER_THRESHOLD = 20;
    // upper bound for audio length
    public static final int UPPER_THRESHOLD = 60;

    public static void main(String[] args) {
        getResult();
    }

    /**
     * get all valid file in the directory
     * @return list of valid file.
     */
    public static List<File> getResult() {
        Server server;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src\\com\\company\\api"));
            server = new Server(bufferedReader.readLine());
        } catch (IOException exception) {
            System.out.println("api file not found");
            return null;
        }

        // get all files in given directory
        File[] fileArr= new File(FOLDER_PATH).listFiles();
        if (fileArr == null) return null;
        // to store valid file (mp3, within time limit, correctly named)
        List<File> validFileList = new ArrayList<>();
        for (File file : fileArr) {
            System.out.println("=============================");
            System.out.println("processing file: " + file.getName());
            if (isFileNameValid(file) && isDurationValid(file)) {
                System.out.println("Final Score: " + server.getScore(file));
            }
            System.out.println("=============================\n");
        }
        return validFileList;
    }
    /**
     * get duration of audio file in second.
     * catch UnsupportedAudioFileException and IOException
     *
     * code below is derived from
     * https://stackoverflow.com/questions/3046669/how-do-i-get-a-mp3-files-total-time-in-java/3056161
     *
     * @param file audio file that need to get duration in second
     * @return the duration or -1 if any exception occurs.
     */
    private static boolean isDurationValid(File file){
        try {
            AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
            int length =  (int) (((Long) baseFileFormat.properties().get("duration") / 1000) / 1000) % 60;
            if (length < LOWER_THRESHOLD) {
                System.out.println("Error: time below lower bound");
                return false;
            } else if (length > UPPER_THRESHOLD) {
                System.out.println("Error: time exceeds upper bound");
                return false;
            }
            return true;
        } catch (UnsupportedAudioFileException | IOException unsupportedAudioFileException) {
            System.out.println("Error: " + unsupportedAudioFileException.toString());
            return false;
        }
    }
    /**
     * check if given file is of type  mp3
     *
     * @param file given file
     * @return whether file is valid mp3
     */
    private static boolean isFileNameValid(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && fileName.substring(dotIndex + 1).equalsIgnoreCase("mp3")) {
            return true;
        } else {
            System.out.println("Error: file name invalid.");
            return false;
        }
    }
}
