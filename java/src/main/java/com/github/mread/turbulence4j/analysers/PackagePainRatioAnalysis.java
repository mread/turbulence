package com.github.mread.turbulence4j.analysers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.calculators.DistinctIssueCommitsPerPackageCalculator;
import com.github.mread.turbulence4j.calculators.IssueTypeCalculator;
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

        IssueTypeCalculator issueTypeCalculator = new IssueTypeCalculator(new File("raw-data-all-frame-2010-12-14.zip"));

        CountPackageMentionsTransformer countPackageMentionsTransformer = new CountPackageMentionsTransformer(
                distinctIssueCommitsPerPackageCalculator, issueTypeCalculator);
        countPackageMentionsTransformer.setPackageDepth(6);
        countPackageMentionsTransformer.setTopN(30);
        countPackageMentionsTransformer.setChangesThreshold(3);
        countPackageMentionsTransformer.setTransformations(getPackageTransformations());

        Output issuesByPackage = new JsonIssuesByPackageOutputWriter(new File(destinationDirectory, "js/"),
                countPackageMentionsTransformer);

        configureCalculators(distinctIssueCommitsPerPackageCalculator, issueTypeCalculator);
        configureTransformers(countPackageMentionsTransformer);
        configureOutputs(issuesByPackage);

    }

    private Map<String, String> getPackageTransformations() {
        HashMap<String, String> tx = new HashMap<String, String>();
        tx.put("com.catlin.integration.expressdeclarations", "com.catlin.expressdeclarations");
        tx.put("com.catlin.model.expressdeclarations", "com.catlin.expressdeclarations");
        tx.put("com.catlin.ui.expressdeclarations", "com.catlin.expressdeclarations");
        return tx;
    }

}
