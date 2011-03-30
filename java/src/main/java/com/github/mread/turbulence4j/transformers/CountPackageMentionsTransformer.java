package com.github.mread.turbulence4j.transformers;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.ArrayList;
import java.util.List;

import ch.lambdaj.group.Group;

import com.github.mread.turbulence4j.analysisapi.Transformer;
import com.github.mread.turbulence4j.calculators.DistinctIssueCommitsPerPackageCalculator;
import com.github.mread.turbulence4j.calculators.FileValue;
import com.github.mread.turbulence4j.calculators.IssuePackage;

public class CountPackageMentionsTransformer implements Transformer<List<FileValue>> {

    private final DistinctIssueCommitsPerPackageCalculator distinctIssueCommitsPerPackageCalculator;
    private List<FileValue> results;

    public CountPackageMentionsTransformer(
            DistinctIssueCommitsPerPackageCalculator distinctIssueCommitsPerPackageCalculator) {

        this.distinctIssueCommitsPerPackageCalculator = distinctIssueCommitsPerPackageCalculator;
    }

    @Override
    public void transform() {
        Group<IssuePackage> groupedByPackage = group(distinctIssueCommitsPerPackageCalculator.getResults(),
                by(on(IssuePackage.class).getPackageName()));

        results = new ArrayList<FileValue>();
        for (Group<IssuePackage> issuePackageGroup : groupedByPackage.subgroups()) {
            if (issuePackageGroup.getSize() > 3)
                results.add(new FileValue(issuePackageGroup.first().getPackageName(), issuePackageGroup.getSize()));
        }
        results = sort(results, on(FileValue.class).getNegativeValue());
    }

    @Override
    public List<FileValue> getResults() {
        return results;
    }

}
