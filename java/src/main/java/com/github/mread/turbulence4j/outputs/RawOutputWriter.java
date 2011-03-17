package com.github.mread.turbulence4j.outputs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.analysisapi.Transformer;

public class RawOutputWriter implements Output {

    static final String RAW_OUTPUT_TXT = "raw-output.txt";

    private final File destinationDirectory;
    private final Transformer<Map<String, int[]>> transformer;

    public RawOutputWriter(File destinationDirectory, Transformer<Map<String, int[]>> transformer) {
        this.destinationDirectory = destinationDirectory;
        this.transformer = transformer;
    }

    @Override
    public void output() {
        try {
            write(transformer.getResults());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(Map<String, int[]> richData) throws IOException {
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

    @Override
    public void setRange(String range) {
        // not interested in range
    }

}
