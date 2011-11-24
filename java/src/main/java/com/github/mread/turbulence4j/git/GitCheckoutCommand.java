package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class GitCheckoutCommand extends BaseGitCommand<Void> {

    public GitCheckoutCommand(File workingDirectory, String treeish) {
        super(workingDirectory, "checkout " + treeish);
    }

    @Override
    public Void call() {
        return runGit().withStdErrProcessor(dumpAllToConsole());
    }

    private Callback<Void> dumpAllToConsole() {
        return new Callback<Void>() {
            @Override
            public Void execute(BufferedReader reader) {
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (!line.isEmpty()) {
                            System.out.println(line);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
    }

}
