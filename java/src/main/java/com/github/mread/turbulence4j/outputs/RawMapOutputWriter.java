package com.github.mread.turbulence4j.outputs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.calculators.AuthorFilenameKey;
import com.github.mread.turbulence4j.transformers.NoopMapTransformer;

public class RawMapOutputWriter implements Output {

    static final String RAW_OUTPUT_TXT = "raw-map-output.txt";
    private final File destinationDirectory;
    private final NoopMapTransformer noopTransformer;

    public RawMapOutputWriter(File destinationDirectory, NoopMapTransformer noopTransformer) {
        this.destinationDirectory = destinationDirectory;
        this.noopTransformer = noopTransformer;
    }

    @Override
    public void output() {
        try {
            write(noopTransformer.getResults());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(Map<AuthorFilenameKey, Integer> result) throws IOException {
        File rawOutput = new File(destinationDirectory, RAW_OUTPUT_TXT);
        destinationDirectory.mkdirs();
        rawOutput.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(rawOutput);
        for (AuthorFilenameKey authorFilename : result.keySet()) {
            fileOutputStream.write((authorFilename.getAuthor() + "\t").getBytes());
            fileOutputStream.write((authorFilename.getFilename() + "\t").getBytes());
            fileOutputStream.write((result.get(authorFilename) + "\n").getBytes());
        }
        fileOutputStream.close();
    }

}
