package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitAdapter {

    private static final String GIT_STATUS = "git status";
    private static final String GIT_LOG = "git log --all --numstat --format=%n --relative";

    public boolean isRepo(File directory) {
        BufferedReader errorReader = errorReaderForGitCommand(GIT_STATUS, directory);
        return !readerContainsText(errorReader, "Not a git repository");
    }

    public BufferedReader getLog(File directory) {
        return readerForGitCommand(GIT_LOG, directory);
    }

    private boolean readerContainsText(BufferedReader reader, String textToMatch) {
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.contains(textToMatch)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private BufferedReader readerForGitCommand(String gitCommand, File directory) {
        Process process = getProcessForGitCommand(gitCommand, directory);
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    private BufferedReader errorReaderForGitCommand(String gitCommand, File directory) {
        Process process = getProcessForGitCommand(gitCommand, directory);
        return new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    private Process getProcessForGitCommand(String gitCommand, File directory) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(gitCommand, null, directory);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return process;
    }

}
