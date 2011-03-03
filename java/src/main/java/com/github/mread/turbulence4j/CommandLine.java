package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.git.GitAdapter;

public class CommandLine {

    public static final String OUTPUT_DIRECTORY = "t4j-output/";

    private final GitAdapter gitAdapter;
    private final File workingDirectory;
    private File outputDirectory;

    public static void main(String[] args) {
        if (args.length == 0) {
            new CommandLine(".").execute();
        } else {
            new CommandLine(args[0]).execute();
        }
    }

    public CommandLine(String workingDirectoryPath) {
        this(new GitAdapter(), workingDirectoryPath);
    }

    public CommandLine(GitAdapter gitAdapter, String workingDirectoryPath) {
        this.gitAdapter = gitAdapter;
        this.workingDirectory = new File(workingDirectoryPath);
    }

    public void execute() {
        if (!isGitRepository()) {
            throw new RuntimeException("Not a git repo: " + workingDirectory.getAbsolutePath());
        }
        makeOutputDirectory();
    }

    private void makeOutputDirectory() {
        outputDirectory = new File(workingDirectory, OUTPUT_DIRECTORY);
        outputDirectory.mkdirs();
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    boolean isGitRepository() {
        return gitAdapter.isRepo(workingDirectory);
    }

}
