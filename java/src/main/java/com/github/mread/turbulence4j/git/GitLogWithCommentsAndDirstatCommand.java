package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitLogWithCommentsAndDirstatCommand extends BaseGitLogCommand<List<String>> {

    public GitLogWithCommentsAndDirstatCommand(File workingDirectory, String range) {
        super(workingDirectory,
                "log -w --no-merges --relative --format=\"format:%s\" --dirstat",
                range);
    }

    @Override
    public List<String> call() {
        return runGit().withStdOutProcessor(trimEmptyLinesFromAndProcessLog());
    }

    private Callback<List<String>> trimEmptyLinesFromAndProcessLog() {
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
