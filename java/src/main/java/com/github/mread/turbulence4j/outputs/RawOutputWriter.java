package com.github.mread.turbulence4j.outputs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.analysisapi.TransformerResults;
import com.github.mread.turbulence4j.transformers.FileResultsMergeTransformer;

public class RawOutputWriter implements Output {

    static final String RAW_OUTPUT_TXT = "raw-output.txt";
    private final File destinationDirectory;

    public RawOutputWriter(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    public void run(TransformerResults transformerResults) {
        Map<String, int[]> result = transformerResults.get(FileResultsMergeTransformer.class).getResult();
        try {
            write(result);
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

}
