package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GitShowCommand extends BaseGitCommand<File> {

    public GitShowCommand(File workingDirectory, String sha1, String targetFile) {
        super(workingDirectory, "show -w --no-merges " + sha1 + ":" + targetFile);
    }

    @Override
    public File call() {
        return runGit().withStdOutProcessor(returnByteArray());
    }

    private Callback<File> returnByteArray() {
        return new Callback<File>() {
            @Override
            public File execute(BufferedReader reader) {
                String line = null;
                try {
                    File tempFile = File.createTempFile("t4j", null);
                    tempFile.deleteOnExit();
                    FileOutputStream output = new FileOutputStream(tempFile);
                    while ((line = reader.readLine()) != null) {
                        output.write((line + "\n").getBytes());
                    }
                    output.close();
                    return tempFile;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
