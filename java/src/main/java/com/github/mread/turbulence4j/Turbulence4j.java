package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.analysers.ChurnByAuthorAnalysis;
import com.github.mread.turbulence4j.analysers.ChurnComplexityAnalysis;
import com.github.mread.turbulence4j.analysers.ComplexityByAuthorAnalysis;
import com.github.mread.turbulence4j.analysers.ComplexityToChurnRatioAnalysis;
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
        ChurnComplexityAnalysis churnComplexityAnalysis = new ChurnComplexityAnalysis(gitAdapter, outputDirectory);
        ChurnByAuthorAnalysis churnByAuthorAnalysis = new ChurnByAuthorAnalysis(gitAdapter, outputDirectory);
        ComplexityByAuthorAnalysis complexityByAuthorAnalysis = new ComplexityByAuthorAnalysis(gitAdapter,
            outputDirectory);
        ComplexityToChurnRatioAnalysis complexityToChurnRatioAnalysis =
                new ComplexityToChurnRatioAnalysis(gitAdapter, outputDirectory);
        // PackagePainRatioAnalysis packagePainRatioAnalysis = new
        // PackagePainRatioAnalysis(
        // gitAdapter,
        // workingDirectory,
        // outputDirectory);
        analysisRepository.register(churnComplexityAnalysis);
        analysisRepository.register(churnByAuthorAnalysis);
        analysisRepository.register(complexityByAuthorAnalysis);
        analysisRepository.register(complexityToChurnRatioAnalysis);
        // analysisRepository.register(packagePainRatioAnalysis);
        analysisEngine = new AnalysisEngine(analysisRepository).forRange(range);
    }

    boolean isGitRepository() {
        return gitAdapter.isRepo(workingDirectory);
    }

}
