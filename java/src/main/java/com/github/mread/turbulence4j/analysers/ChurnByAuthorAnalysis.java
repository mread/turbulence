package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.calculators.AuthorFilenameKey;
import com.github.mread.turbulence4j.calculators.ChurnByAuthorCalculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.outputs.JsonChurnByAuthorOutputWriter;
import com.github.mread.turbulence4j.outputs.RawMapOutputWriter;
import com.github.mread.turbulence4j.transformers.NoopMapTransformer;

public class ChurnByAuthorAnalysis extends BaseAnalysis {

    private final GitAdapter gitAdapter;
    private final File destinationDirectory;

    public ChurnByAuthorAnalysis(GitAdapter gitAdapter, File destinationDirectory) {

        this.gitAdapter = gitAdapter;
        this.destinationDirectory = destinationDirectory;

    }

    @Override
    public void configure(File targetDirectory, JavaFileFinder fileFinder) {
        ChurnByAuthorCalculator churnCalculator = new ChurnByAuthorCalculator(targetDirectory, fileFinder, gitAdapter);
        NoopMapTransformer<AuthorFilenameKey, Integer> noopTransformer = new NoopMapTransformer<AuthorFilenameKey, Integer>(
            churnCalculator);
        JsonChurnByAuthorOutputWriter jsonOutputWriter = new JsonChurnByAuthorOutputWriter(
            new File(destinationDirectory, "js/"),
            noopTransformer);
        RawMapOutputWriter rawMapOutputWriter = new RawMapOutputWriter(destinationDirectory, noopTransformer);

        configureCalculators(churnCalculator);
        configureTransformers(noopTransformer);
        configureOutputs(jsonOutputWriter, rawMapOutputWriter);

    }
}
