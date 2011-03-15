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

    public List<String> getLogOfSha1s(File directory) {
        return new GitLogOfSha1sCommand(directory).call();
    }

    public byte[] doShow(File directory, String sha1, String targetFile) {
        return new GitShowCommand(directory, sha1, targetFile).call();
    }

    public List<String> getFilesForACommit(File directory, String sha1) {
        return new GitShowFilesForACommitCommand(directory, sha1).call();
    }

}
