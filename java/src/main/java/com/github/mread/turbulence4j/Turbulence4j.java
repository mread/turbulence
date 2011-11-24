package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.analysers.ComplexityOverTimeAnalysis;
import com.github.mread.turbulence4j.analysisapi.AnalysisEngine;
import com.github.mread.turbulence4j.analysisapi.AnalysisRepository;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.google.inject.Provider;

public class Turbulence4j {
    private final File workingDirectory;
    private final File outputDirectory;
    private final GitAdapter gitAdapter;
    private final String range;
    private AnalysisEngine analysisEngine;
    private final Provider<AnalysisRepository> analysisRepositoryProvider;

    public Turbulence4j(File workingDirectory, File outputDirectory, GitAdapter gitAdapter,
            Provider<AnalysisRepository> analysisRepositoryProvider, String range) {
        this(workingDirectory, outputDirectory, gitAdapter, null, analysisRepositoryProvider, range);
    }

    Turbulence4j(File workingDirectory, File outputDirectory, GitAdapter gitAdapter,
            AnalysisEngine analysisEngine, Provider<AnalysisRepository> analysisRepositoryProvider, String range) {
        this.workingDirectory = workingDirectory;
        this.outputDirectory = outputDirectory;
        this.gitAdapter = gitAdapter;
        this.analysisEngine = analysisEngine;
        this.analysisRepositoryProvider = analysisRepositoryProvider;
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

    void initialiseAnalysisEngine() {
        AnalysisRepository analysisRepository = analysisRepositoryProvider.get();
        analysisRepository.setWorkingDirectory(workingDirectory);
        analysisRepository.setFileFinder(new JavaFileFinder(workingDirectory, "target"));

        // PackagePainRatioAnalysis packagePainRatioAnalysis = new
        // PackagePainRatioAnalysis(
        // gitAdapter,
        // workingDirectory,
        // outputDirectory);
        analysisRepository.register(new ComplexityOverTimeAnalysis(gitAdapter, outputDirectory));
//        analysisRepository.register(new ChurnComplexityAnalysis(gitAdapter, outputDirectory));
//        analysisRepository.register(new ChurnByAuthorAnalysis(gitAdapter, outputDirectory));
//        analysisRepository.register(new ComplexityByAuthorAnalysis(gitAdapter, outputDirectory));
//        analysisRepository.register(new ComplexityToChurnRatioAnalysis(gitAdapter, outputDirectory));
        // analysisRepository.register(packagePainRatioAnalysis);
        analysisEngine = new AnalysisEngine(analysisRepository).forRange(range);
    }

    boolean isGitRepository() {
        return gitAdapter.isRepo(workingDirectory);
    }

}
