package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.calculators.ChurnCalculator;
import com.github.mread.turbulence4j.calculators.ComplexityCalculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.output.OutputWriter;

public class Turbulence4j {

    private final OutputWriter outputWriter;
    private final GitAdapter gitAdapter;
    private final File workingDirectory;
    private final ComplexityCalculator complexityCalculator;
    private final ChurnCalculator churnCalculator;
    private int totalComplexity;
    private int totalChurn;

    public Turbulence4j(OutputWriter outputWriter, GitAdapter gitAdapter, File workingDirectory) {
        this.outputWriter = outputWriter;
        this.gitAdapter = gitAdapter;
        this.workingDirectory = workingDirectory;
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
        outputWriter.write(churnCalculator.getResults(), complexityCalculator.getResults());

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
