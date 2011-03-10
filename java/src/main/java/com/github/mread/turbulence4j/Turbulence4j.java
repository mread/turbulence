package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.analysers.ChurnComplexityAnalysis;
import com.github.mread.turbulence4j.analysisapi.Analysis;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class Turbulence4j {

    private final GitAdapter gitAdapter;
    private final File workingDirectory;
    private final Analysis analysis;

    public Turbulence4j(File workingDirectory, File outputDirectory, GitAdapter gitAdapter) {
        this(workingDirectory,
                gitAdapter,
                new ChurnComplexityAnalysis(
                        workingDirectory,
                        new JavaFileFinder(workingDirectory, "target"),
                        gitAdapter,
                        outputDirectory));
    }

    public Turbulence4j(File workingDirectory, GitAdapter gitAdapter, Analysis analysis) {
        this.workingDirectory = workingDirectory;
        this.gitAdapter = gitAdapter;
        this.analysis = analysis;
    }

    public void execute() {
        if (!isGitRepository()) {
            throw new RuntimeException("Not a git repo: " + workingDirectory.getAbsolutePath());
        }
        analysis.run();
    }

    boolean isGitRepository() {
        return gitAdapter.isRepo(workingDirectory);
    }

}
