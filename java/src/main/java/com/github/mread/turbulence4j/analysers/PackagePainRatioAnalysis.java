package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.calculators.DistinctIssueCommitsPerPackageCalculator;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.outputs.JsonIssuesByPackageOutputWriter;
import com.github.mread.turbulence4j.transformers.CountPackageMentionsTransformer;

public class PackagePainRatioAnalysis extends BaseAnalysis {

    private final GitAdapter gitAdapter;
    private final File targetDirectory;
    private final File destinationDirectory;

    public PackagePainRatioAnalysis(GitAdapter gitAdapter, File targetDirectory, File destinationDirectory) {
        this.gitAdapter = gitAdapter;
        this.targetDirectory = targetDirectory;
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    public void configure() {

        DistinctIssueCommitsPerPackageCalculator distinctIssueCommitsPerPackageCalculator =
                new DistinctIssueCommitsPerPackageCalculator(gitAdapter, targetDirectory);

        CountPackageMentionsTransformer countPackageMentionsTransformer = new CountPackageMentionsTransformer(
                distinctIssueCommitsPerPackageCalculator);

        Output issuesByPackage = new JsonIssuesByPackageOutputWriter(new File(destinationDirectory, "js/"),
                countPackageMentionsTransformer);

        configureCalculators(distinctIssueCommitsPerPackageCalculator);
        configureTransformers(countPackageMentionsTransformer);
        configureOutputs(issuesByPackage);

    }

}
