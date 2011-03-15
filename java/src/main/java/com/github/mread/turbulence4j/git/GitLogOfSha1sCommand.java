package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitLogOfSha1sCommand extends BaseGitCommand<List<String>> {

    public GitLogOfSha1sCommand(File workingDirectory) {
        super(workingDirectory, "log --all --no-merges -w " +
                "--format=\"%H|%P|%aN\" " +
                "11.01..11.02");
    }

    @Override
    public List<String> call() {
        return runGit().withStdOutProcessor(noop());
    }

    private Callback<List<String>> noop() {
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
