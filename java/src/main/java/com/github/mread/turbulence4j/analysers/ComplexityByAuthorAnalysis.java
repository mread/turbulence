package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.calculators.complexitycontribution.ComplexityContributionCalculator;
import com.github.mread.turbulence4j.calculators.complexitycontribution.ComplexityContributionProcessor;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.outputs.JsonComplexityByAuthorOutputWriter;

public class ComplexityByAuthorAnalysis extends BaseAnalysis {

    private final GitAdapter gitAdapter;
    private final File destinationDirectory;

    public ComplexityByAuthorAnalysis(GitAdapter gitAdapter, File destinationDirectory) {
        this.gitAdapter = gitAdapter;
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    public void configure(File targetDirectory, JavaFileFinder fileFinder) {
        ComplexityContributionCalculator complexityContributionCalculator = new ComplexityContributionCalculator(
            targetDirectory,
            gitAdapter,
            new ComplexityContributionProcessor(gitAdapter, targetDirectory, fileFinder));

        JsonComplexityByAuthorOutputWriter jsonOutput = new JsonComplexityByAuthorOutputWriter(
            new File(destinationDirectory, "js/"),
            complexityContributionCalculator);

        configureCalculators(complexityContributionCalculator);
        configureOutputs(jsonOutput);

    }
}
