package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitLogOfSha1sCommand extends BaseGitLogCommand<List<String>> {

    public GitLogOfSha1sCommand(File workingDirectory, String range) {
        super(workingDirectory, "log --no-merges -w " +
                "--pretty=format:%H|%P|%aN",
                range);
    }

    @Override
    public List<String> call() {
        return runGit().withStdOutProcessor(getSha1s());
    }

    private Callback<List<String>> getSha1s() {
        return new Callback<List<String>>() {
            @Override
            public List<String> execute(BufferedReader reader) {
                List<String> result = new ArrayList<String>();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        result.add(line);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return result;
            }
        };
    }
}
