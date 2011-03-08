package com.github.mread.turbulence4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class RawOutputWriter implements CanWriteOutput {

    static final String RAW_OUTPUT_TXT = "raw-output.txt";
    private final File destinationDirectory;

    public RawOutputWriter(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void write(Map<String, int[]> richData) throws IOException {
        File rawOutput = new File(destinationDirectory, RAW_OUTPUT_TXT);
        destinationDirectory.mkdirs();
        rawOutput.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(rawOutput);
        for (String file : richData.keySet()) {
            fileOutputStream.write((file + "\t").getBytes());
            fileOutputStream.write((richData.get(file)[0] + "\t").getBytes());
            fileOutputStream.write((richData.get(file)[1] + "\n").getBytes());
        }
        fileOutputStream.close();
    }
}
