package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.calculators.complexityovertime.CodeComplexityAtEachCommitCalculator;
import com.github.mread.turbulence4j.calculators.complexityovertime.JsonCodeComplexityAtEachCommitOutputWriter;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ComplexityOverTimeAnalysis extends BaseAnalysis {

    private final GitAdapter gitAdapter;
    private final File destinationDirectory;

    public ComplexityOverTimeAnalysis(GitAdapter gitAdapter, File destinationDirectory) {
        this.gitAdapter = gitAdapter;
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    public void configure(File targetDirectory, JavaFileFinder fileFinder) {
        
        CodeComplexityAtEachCommitCalculator codeComplexityAtEachCommitCalculator = new CodeComplexityAtEachCommitCalculator(
            targetDirectory,
            gitAdapter);

        JsonCodeComplexityAtEachCommitOutputWriter jsonOutput = new JsonCodeComplexityAtEachCommitOutputWriter(
            new File(destinationDirectory, "js/"),
            codeComplexityAtEachCommitCalculator);

        configureCalculators(codeComplexityAtEachCommitCalculator);
        configureOutputs(jsonOutput);

    }
}
