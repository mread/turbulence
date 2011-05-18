package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class BaseGitCommand<T> implements GitCommand<T> {

    public interface Callback<T> {
        T execute(BufferedReader reader);
    }

    //    private static final String GIT_PATH = "/work/apps/git/bin/git ";
    private static final String GIT_PATH = "git ";

    private final String gitCommandLine;
    private final File workingDirectory;
    private final String range;
    private BufferedReader stdOut;
    private BufferedReader stdErr;
    private Process process;

    public BaseGitCommand(File workingDirectory, String gitCommandLine, String range) {
        this.workingDirectory = workingDirectory;
        this.gitCommandLine = gitCommandLine;
        this.range = range;
    }

    public BaseGitCommand(File workingDirectory, String gitCommandLine) {
        this(workingDirectory, gitCommandLine, "");
    }

    protected BaseGitCommand<T> runGit() {
        Runtime runtime = Runtime.getRuntime();
        try {
            String commandLine = constructCommand();
            process = runtime.exec(commandLine, null, workingDirectory);
            //            System.out.println(commandLine);
            this.stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            this.stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private String constructCommand() {
        return GIT_PATH + gitCommandLine + " " + range;
    }

    private void closeStdOut() {
        try {
            getStdOut().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeStdErr() {
        try {
            getStdErr().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected T withStdErrProcessor(Callback<T> callback) {
        closeStdOut();
        T result = callback.execute(getStdErr());
        closeAllReaders();
        return result;
    }

    public T withStdOutProcessor(Callback<T> callback) {
        closeStdErr();
        T result = callback.execute(getStdOut());
        closeAllReaders();
        return result;
    }

    void closeAllReaders() {
        try {
            closeStdOut();
            closeStdErr();
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    BufferedReader getStdOut() {
        return stdOut;
    }

    BufferedReader getStdErr() {
        return stdErr;
    }
}