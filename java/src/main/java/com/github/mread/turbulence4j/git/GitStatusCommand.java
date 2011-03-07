package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class GitStatusCommand extends BaseGitCommand<Boolean> {

    public GitStatusCommand(File workingDirectory) {
        super(workingDirectory, "status");
    }

    @Override
    public Boolean call() {
        return runGit().withStdErrProcessor(isAGitRepository());
    }

    private Callback<Boolean> isAGitRepository() {
        return new Callback<Boolean>() {
            @Override
            public Boolean execute(BufferedReader reader) {
                return !contains("Not a git repository", reader);
            }
        };
    }

    protected boolean contains(String stringToMatch, BufferedReader reader) {
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.contains(stringToMatch)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
