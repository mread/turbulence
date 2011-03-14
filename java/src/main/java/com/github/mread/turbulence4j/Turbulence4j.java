package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.analysers.ChurnComplexityAnalysis;
import com.github.mread.turbulence4j.analysisapi.AnalysisEngine;
import com.github.mread.turbulence4j.analysisapi.AnalysisRepository;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class Turbulence4j {

    private final File workingDirectory;
    private final File outputDirectory;
    private final GitAdapter gitAdapter;
    private AnalysisEngine analysisEngine;

    public Turbulence4j(File workingDirectory, File outputDirectory, GitAdapter gitAdapter) {
        this(workingDirectory, outputDirectory, gitAdapter, null);
    }

    Turbulence4j(File workingDirectory, File outputDirectory, GitAdapter gitAdapter,
            AnalysisEngine analysisEngine) {
        this.workingDirectory = workingDirectory;
        this.outputDirectory = outputDirectory;
        this.gitAdapter = gitAdapter;
        this.analysisEngine = analysisEngine;
    }

    public void execute() {
        if (!isGitRepository()) {
            throw new RuntimeException("Not a git repo: " + workingDirectory.getAbsolutePath());
        }
        if (analysisEngine == null) {
            initialiseAnalysisEngine();
        }
        analysisEngine.runAll();
    }

    private void initialiseAnalysisEngine() {
        AnalysisRepository analysisRepository = new AnalysisRepository();
        ChurnComplexityAnalysis churnComplexityAnalysis = new ChurnComplexityAnalysis(
                workingDirectory,
                new JavaFileFinder(workingDirectory, "target"),
                gitAdapter,
                outputDirectory);
        analysisRepository.register(churnComplexityAnalysis);
        analysisEngine = new AnalysisEngine(analysisRepository);
    }

    boolean isGitRepository() {
        return gitAdapter.isRepo(workingDirectory);
    }

}
