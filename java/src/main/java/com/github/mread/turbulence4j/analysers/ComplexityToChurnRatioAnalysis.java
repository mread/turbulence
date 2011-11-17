package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.calculators.complexitycontribution.ComplexityContributionCalculator;
import com.github.mread.turbulence4j.calculators.complexitycontribution.ComplexityToChurnRatioProcessor;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.outputs.JsonComplexityToChurnRatioOutputWriter;

public class ComplexityToChurnRatioAnalysis extends BaseAnalysis {

    private final GitAdapter gitAdapter;
    private final File destinationDirectory;

    public ComplexityToChurnRatioAnalysis(GitAdapter gitAdapter,
            File destinationDirectory) {
        this.gitAdapter = gitAdapter;
        this.destinationDirectory = destinationDirectory;

    }

    @Override
    public void configure(File targetDirectory, JavaFileFinder fileFinder) {
        ComplexityContributionCalculator complexityContributionCalculator = new ComplexityContributionCalculator(
            targetDirectory,
            gitAdapter,
            new ComplexityToChurnRatioProcessor(gitAdapter, targetDirectory, fileFinder));

        JsonComplexityToChurnRatioOutputWriter jsonOutput = new JsonComplexityToChurnRatioOutputWriter(
            new File(destinationDirectory, "js/"),
            complexityContributionCalculator);

        configureCalculators(complexityContributionCalculator);
        configureOutputs(jsonOutput);

    }
}
