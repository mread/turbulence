package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitShowFilesForACommitCommand extends BaseGitCommand<List<String>> {

    public GitShowFilesForACommitCommand(File workingDirectory, String sha1) {
        super(workingDirectory, "show -w --no-merges --name-only " +
                "--format=\"format:\" " + sha1);
    }

    @Override
    public List<String> call() {
        return runGit().withStdOutProcessor(stripBlankLines());
    }

    private Callback<List<String>> stripBlankLines() {
        return new Callback<List<String>>() {
            @Override
            public List<String> execute(BufferedReader reader) {
                List<String> output = new ArrayList<String>();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (!line.isEmpty()) {
                            output.add(line);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return output;
            }
        };
    }

}
