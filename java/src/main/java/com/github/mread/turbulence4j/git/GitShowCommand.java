package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class GitShowCommand extends BaseGitLogCommand<byte[]> {

    public GitShowCommand(File workingDirectory, String sha1, String targetFile) {
        super(workingDirectory, "show -w " + sha1 + ":" + targetFile);
    }

    @Override
    public byte[] call() {
        return runGit().withStdOutProcessor(returnByteArray());
    }

    private Callback<byte[]> returnByteArray() {
        return new Callback<byte[]>() {
            @Override
            public byte[] execute(BufferedReader reader) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        outputStream.write((line + "\n").getBytes());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return outputStream.toByteArray();
            }
        };
    }

}
