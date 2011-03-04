package com.github.mread.turbulence4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.mread.calculators.ChurnCalculator;
import com.github.mread.calculators.ComplexityCalculator;
import com.github.mread.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class CommandLine {

    public static final String OUTPUT_DIRECTORY = "t4j-output/";

    private final GitAdapter gitAdapter;
    private final File workingDirectory;
    private final ComplexityCalculator complexityCalculator;
    private final ChurnCalculator churnCalculator;
    private File outputDirectory;
    private int totalComplexity;
    private int totalChurn;

    public static final String RAW_OUTPUT_TXT = "raw-output.txt";

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

    // manually wiring here - would expect Guice one day
    public CommandLine(GitAdapter gitAdapter, String workingDirectoryPath) {
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
        makeOutputDirectory();
        totalComplexity = complexityCalculator.calculate();
        totalChurn = churnCalculator.calculate();
        writeToRawOutput();
    }

    private void writeToRawOutput() {
        try {
            File rawOutput = new File(outputDirectory, RAW_OUTPUT_TXT);
            rawOutput.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(rawOutput);
            fileOutputStream.write(("Total complexity: " + totalComplexity + "\n").getBytes());
            fileOutputStream.write(("Total churn: " + totalChurn + "\n").getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public int getTotalComplexity() {
        return totalComplexity;
    }

    public int getTotalChurn() {
        return totalChurn;
    }

}
