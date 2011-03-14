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

    private final File targetDirectory;
    private final JavaFileFinder javaFileFinder;
    private final GitAdapter gitAdapter;
    private final File destinationDirectory;

    public ChurnComplexityAnalysis(File targetDirectory,
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
        ChurnCalculator churnCalculator = new ChurnCalculator(targetDirectory, javaFileFinder, gitAdapter);
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(javaFileFinder);
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
