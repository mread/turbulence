package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.calculators.ChurnCalculator;
import com.github.mread.turbulence4j.calculators.ComplexityCalculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.outputs.JsonOutputWriter;
import com.github.mread.turbulence4j.outputs.RawOutputWriter;
import com.github.mread.turbulence4j.transformers.MergeMapsTransformer;

public class ChurnComplexityAnalysis extends BaseAnalysis {

    private final GitAdapter gitAdapter;
    private final File destinationDirectory;

    public ChurnComplexityAnalysis(GitAdapter gitAdapter,
            File destinationDirectory) {

        this.gitAdapter = gitAdapter;
        this.destinationDirectory = destinationDirectory;

    }

    @Override
    public void configure(File targetDirectory, JavaFileFinder fileFinder) {
        ChurnCalculator churnCalculator = new ChurnCalculator(targetDirectory, fileFinder, gitAdapter);
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(fileFinder);
        MergeMapsTransformer mergeMapsTransformer = new MergeMapsTransformer(churnCalculator, complexityCalculator);
        JsonOutputWriter jsonOutputWriter = new JsonOutputWriter(
            new File(destinationDirectory, "js/"),
            mergeMapsTransformer);
        RawOutputWriter rawOutputWriter = new RawOutputWriter(destinationDirectory, mergeMapsTransformer);

        configureCalculators(churnCalculator, complexityCalculator);
        configureTransformers(mergeMapsTransformer);
        configureOutputs(jsonOutputWriter, rawOutputWriter);
    }

}
