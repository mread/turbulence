package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.mread.turbulence4j.git.BaseGitCommand.Callback;

public class GitVersionCommand extends BaseGitCommand<Void> {

    public GitVersionCommand(File workingDirectory) {
        super(workingDirectory, "--version");
    }

    @Override
    public Void call() {
        return runGit().withStdOutProcessor(dumpAllToConsole());
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
