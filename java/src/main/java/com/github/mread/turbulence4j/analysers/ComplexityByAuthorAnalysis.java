package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.calculators.complexitycontribution.ComplexityContributionCalculator;
import com.github.mread.turbulence4j.calculators.complexitycontribution.ComplexityContributionProcessor;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.outputs.JsonComplexityByAuthorOutputWriter;

public class ComplexityByAuthorAnalysis extends BaseAnalysis {

    private final File targetDirectory;
    private final JavaFileFinder javaFileFinder;
    private final GitAdapter gitAdapter;
    private final File destinationDirectory;

    public ComplexityByAuthorAnalysis(File targetDirectory,
            JavaFileFinder javaFileFinder,
            GitAdapter gitAdapter,
            File destinationDirectory) {

        this.targetDirectory = targetDirectory;
        this.javaFileFinder = javaFileFinder;
        this.gitAdapter = gitAdapter;
        this.destinationDirectory = destinationDirectory;

    }

    @Override
    public void configure() {
        ComplexityContributionCalculator complexityContributionCalculator = new ComplexityContributionCalculator(
                targetDirectory,
                gitAdapter,
                new ComplexityContributionProcessor(gitAdapter, targetDirectory, javaFileFinder));

        JsonComplexityByAuthorOutputWriter jsonOutput = new JsonComplexityByAuthorOutputWriter(
                new File(destinationDirectory, "js/"),
                complexityContributionCalculator);

        configureCalculators(complexityContributionCalculator);
        configureOutputs(jsonOutput);

    }
}
