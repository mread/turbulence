package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitLogCommand extends BaseGitCommand<List<String>> {

    public GitLogCommand(File workingDirectory) {
        super(workingDirectory, "log --all -w -M -C --numstat " +
                "--format=%n --relative "
                + "11.01..11.02");
    }

    @Override
    public List<String> call() {
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
