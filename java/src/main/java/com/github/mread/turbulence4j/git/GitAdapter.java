package com.github.mread.turbulence4j.git;

import java.io.File;
import java.util.List;

public class GitAdapter {

    public boolean isRepo(File directory) {
        return new GitStatusCommand(directory).call();
    }

    public List<String> getLog(File directory) {
        return new GitLogCommand(directory).call();
    }

    public List<String> getLogWithAuthor(File directory) {
        return new GitLogWithAuthorCommand(directory).call();
    }
}
