package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.analysers.ChurnByAuthorAnalysis;
import com.github.mread.turbulence4j.analysers.ChurnComplexityAnalysis;
import com.github.mread.turbulence4j.analysers.ComplexityByAuthorAnalysis;
import com.github.mread.turbulence4j.analysers.PackagePainRatioAnalysis;
import com.github.mread.turbulence4j.analysisapi.AnalysisEngine;
import com.github.mread.turbulence4j.analysisapi.AnalysisRepository;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class Turbulence4j {

    private final File workingDirectory;
    private final File outputDirectory;
    private final GitAdapter gitAdapter;
    private final String range;
    private AnalysisEngine analysisEngine;

    public Turbulence4j(File workingDirectory, File outputDirectory, GitAdapter gitAdapter, String range) {
        this(workingDirectory, outputDirectory, gitAdapter, null, range);
    }

    Turbulence4j(File workingDirectory, File outputDirectory, GitAdapter gitAdapter,
                 AnalysisEngine analysisEngine, String range) {
        this.workingDirectory = workingDirectory;
        this.outputDirectory = outputDirectory;
        this.gitAdapter = gitAdapter;
        this.analysisEngine = analysisEngine;
        this.range = range;
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
        ChurnByAuthorAnalysis churnByAuthorAnalysis = new ChurnByAuthorAnalysis(
                workingDirectory,
                new JavaFileFinder(workingDirectory, "target"),
                gitAdapter,
                outputDirectory);
        ComplexityByAuthorAnalysis complexityByAuthorAnalysis = new ComplexityByAuthorAnalysis(
                workingDirectory,
                new JavaFileFinder(workingDirectory, "target"),
                gitAdapter,
                outputDirectory);
        PackagePainRatioAnalysis packagePainRatioAnalysis = new PackagePainRatioAnalysis(
                gitAdapter,
                workingDirectory,
                outputDirectory);
        analysisRepository.register(churnComplexityAnalysis);
        analysisRepository.register(churnByAuthorAnalysis);
        analysisRepository.register(complexityByAuthorAnalysis);
//        analysisRepository.register(packagePainRatioAnalysis);
        analysisEngine = new AnalysisEngine(analysisRepository).forRange(range);
    }

    boolean isGitRepository() {
        return gitAdapter.isRepo(workingDirectory);
    }

}
