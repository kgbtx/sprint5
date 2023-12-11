/**
 * Writes game progress to a file
 */
package com.example.cs449project;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class RecordGame {
    private String fileName;
    private FileWriter writer;

    public RecordGame() {
        generateFilename();
        createFile();
        try {
            writer = new FileWriter(fileName, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void generateFilename() {
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();

        // Define the format for the timestamp in the filename
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        // Format the current time as a string using the specified format
        String timestamp = currentTime.format(formatter);

        // Construct the filename using the formatted timestamp

        fileName = "game_" + timestamp + ".txt";
    }

    public void createFile() {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        fileName = fileName;
    }

    public void record(String log) {
        // Open file for appending
        try {
            writer.write(log + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeFile() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
