package com.github.mread.turbulence4j.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GitAdapter {

    public void getGitVersion(File directory) {
        new GitVersionCommand(directory).call();
    }

    public boolean isRepo(File directory) {
        return new GitStatusCommand(directory).call();
    }

    public List<String> getLog(File directory, String range) {
        return new GitLogCommand(directory, range).call();
    }

    public List<String> getLogWithAuthor(File directory, String range) {
        return new GitLogWithAuthorCommand(directory, range).call();
    }

    public List<String> getLogOfSha1s(File directory, String range) {
        return new GitLogOfSha1sCommand(directory, range).call();
    }

    public List<String> getLogWithCommentsAndDirectories(File directory, String range) {
        return new GitLogWithCommentsAndDirstatCommand(directory, range).call();
    }

    public File doShow(File directory, String sha1, String targetFile) {
        return new GitShowCommand(directory, sha1, targetFile).call();
    }

    public List<String> getFilesForACommit(File directory, String sha1) {
        return new GitShowFilesForACommitCommand(directory, sha1).call();
    }

    public List<String> getCommitChurn(File directory, String sha1) {
        return new GitShowCommitChurnCommand(directory, sha1).call();
    }

    public void checkout(File directory, String treeish) {
        new GitCheckoutCommand(directory, treeish).call();
    }

    public List<CommitParentAuthorTimestamp> parseSha1s(List<String> input) {
        List<CommitParentAuthorTimestamp> results = new ArrayList<CommitParentAuthorTimestamp>();
        for (String line : input) {
            String[] split = line.split("\\|");
            if (split.length == 4) {
                results.add(new CommitParentAuthorTimestamp(split[0], split[1], split[2], Long.parseLong(split[3])));
            } else {
                System.err.println("log output not in expected format: " + line);
            }
        }
        return results;
    }



}
