package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.calculators.ChurnCalculator;
import com.github.mread.calculators.ComplexityCalculator;
import com.github.mread.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class CommandLine {

    public static final String OUTPUT_DIRECTORY = "t4j-output/";

    private final GitAdapter gitAdapter;
    private final OutputWriter outputWriter;
    private final File workingDirectory;
    private final ComplexityCalculator complexityCalculator;
    private final ChurnCalculator churnCalculator;
    private int totalComplexity;
    private int totalChurn;

    public static void main(String[] args) {
        if (args.length == 0) {
            new CommandLine(".").execute();
        } else {
            new CommandLine(args[0]).execute();
        }
    }

    public CommandLine(String workingDirectoryPath) {
        this(new OutputWriter(), new GitAdapter(), workingDirectoryPath);
    }

    // manually wiring here - would expect Guice one day
    public CommandLine(OutputWriter outputWriter, GitAdapter gitAdapter, String workingDirectoryPath) {
        this.outputWriter = outputWriter;
        this.gitAdapter = gitAdapter;
        this.workingDirectory = new File(workingDirectoryPath);
        JavaFileFinder javaFileFinder = new JavaFileFinder(workingDirectory);
        this.complexityCalculator = new ComplexityCalculator(javaFileFinder);
        this.churnCalculator = new ChurnCalculator(workingDirectory, javaFileFinder, gitAdapter);
    }

    public void execute() {
        if (!isGitRepository()) {
            throw new RuntimeException("Not a git repo: " + workingDirectory.getAbsolutePath());
        }
        totalComplexity = complexityCalculator.calculate();
        totalChurn = churnCalculator.calculate();
        outputWriter.write(workingDirectory,
                new File(OUTPUT_DIRECTORY),
                churnCalculator.getResults(),
                complexityCalculator.getResults());
    }

    boolean isGitRepository() {
        return gitAdapter.isRepo(workingDirectory);
    }

    public int getTotalComplexity() {
        return totalComplexity;
    }

    public int getTotalChurn() {
        return totalChurn;
    }

}
