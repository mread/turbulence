package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitLogCommand extends BaseGitLogCommand<List<String>> {

    public GitLogCommand(File workingDirectory, String range) {
        super(workingDirectory, "log -w -M -C --numstat --no-merges " +
                "--format=%n --relative",
                range);
    }

    @Override
    public List<String> call() {
        // return runGit().withStdErrProcessor(trimEmptyLinesFromLog());
        return runGit().withStdOutProcessor(trimEmptyLinesFromLog());
    }

    private Callback<List<String>> trimEmptyLinesFromLog() {
        return new Callback<List<String>>() {
            @Override
            public List<String> execute(BufferedReader reader) {
                List<String> result = new ArrayList<String>();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (!line.isEmpty()) {
                            // System.out.println(line);
                            result.add(line);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return result;
            }
        };
    }

}
