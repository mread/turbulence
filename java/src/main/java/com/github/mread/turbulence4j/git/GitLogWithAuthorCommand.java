package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitLogWithAuthorCommand extends BaseGitLogCommand<List<String>> {

    public GitLogWithAuthorCommand(File workingDirectory) {
        super(workingDirectory, "log --all -w --numstat --format=Author:%aN --relative");
    }

    @Override
    public List<String> call() {
        return runGit().withStdOutProcessor(trimEmptyLinesFromAndProcessAuthorLog());
    }

    private Callback<List<String>> trimEmptyLinesFromAndProcessAuthorLog() {
        return new Callback<List<String>>() {
            @Override
            public List<String> execute(BufferedReader reader) {
                List<String> result = new ArrayList<String>();
                String line = null;
                String author = "";
                try {
                    while ((line = reader.readLine()) != null) {
                        if (!line.isEmpty()) {
                            if (line.startsWith("Author:")) {
                                author = line.substring("Author:".length());
                            } else {
                                String lineWithAuthor = author + "\t" + line;
                                result.add(lineWithAuthor);
                            }
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
