package com.github.mread.turbulence4j.git;

import java.io.File;

public abstract class BaseGitLogCommand<T> extends BaseGitCommand<T> {

    public BaseGitLogCommand(File workingDirectory, String gitCommandLine, String range) {
        super(workingDirectory, gitCommandLine, range);
    }

}