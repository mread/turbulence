package com.github.mread.turbulence4j.calculators.complexitycontribution;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.mread.turbulence4j.calculators.AuthorValue;
import com.github.mread.turbulence4j.calculators.CommitParentAuthorTimestamp;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ComplexityToChurnRatioProcessor extends CommonProcessor {

    private static final long QUANTISE_FACTOR = 10000;

    public ComplexityToChurnRatioProcessor(GitAdapter gitAdapter, File targetDirectory, JavaFileFinder javaFileFinder) {
        super(gitAdapter, targetDirectory, javaFileFinder);
    }

    public List<AuthorValue> process(List<CommitParentAuthorTimestamp> commits) {
        int totalNumberOfCommits = commits.size();
        int commitProgress = 0;
        List<Integer> complexities = new ArrayList<Integer>(totalNumberOfCommits);
        List<Double> ratios = new ArrayList<Double>(totalNumberOfCommits);
        for (CommitParentAuthorTimestamp commit : commits) {
            List<String> modifiedFiles = filesInACommit(commit.getCommit());
            reportProgress(++commitProgress, totalNumberOfCommits, modifiedFiles.size());
            if (modifiedFiles.size() > 200) {
                System.out.println("Ignoring large commit: " + commit.getCommit());
                continue;
            }
            int netFilesetComplexity = calculateNetComplexityDelta(commit, modifiedFiles);
            int netChurn = calculateNetChurn(commit, modifiedFiles);

            if (netFilesetComplexity != 0 && meetsNetComplexityThreshold(netFilesetComplexity)) {
                complexities.add(netFilesetComplexity);
            }
            if (netChurn != 0) {
                double ratio = ((double) netFilesetComplexity) / netChurn;
                if (ratio > 1d) {
                    System.out.println("Ratio > 1: " + netFilesetComplexity + " / " + netChurn + " = " + ratio + " : "
                            + commit.getCommit());
                }
                ratios.add(ratio);
            }
        }
        // return putComplexitiesOutputIntoBuckets(complexities);
        return putRatiosOutputIntoBuckets(ratios);
    }

    private List<AuthorValue> putComplexitiesOutputIntoBuckets(List<Integer> complexities) {

        List<AuthorValue> output = new ArrayList<AuthorValue>();

        Histogram histogram = new Histogram(
                new long[] { -10000, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50, 10000 });

        for (Integer complexity : complexities) {
            histogram.addObservation(complexity);
        }

        for (int i = 0; i < histogram.getSize(); i++) {
            long count = histogram.getCountAt(i);
            if (count != 0) {
                output.add(new AuthorValue(convertToLabel(histogram, i), count));
            }
        }
        return output;
    }

    private List<AuthorValue> putRatiosOutputIntoBuckets(List<Double> ratios) {

        List<AuthorValue> output = new ArrayList<AuthorValue>();

        long[] buckets = new long[43];
        buckets[0] = -1000000;
        buckets[42] = 1000000;
        for (int i = -20; i <= 20; i++) {
            buckets[i + 21] = i * 1000;
        }

        Histogram histogram = new Histogram(
                buckets);

        for (Double ratio : ratios) {
            if (!histogram.addObservation(quantise(ratio))) {
                throw new RuntimeException("Missing bucket for: " + quantise(ratio));
            }
        }

        for (int i = 0; i < histogram.getSize(); i++) {
            long count = histogram.getCountAt(i);
            if (count != 0) {
                output.add(new AuthorValue(convertToRatioLabel(histogram, i), count));
            }
        }
        return output;
    }

    private String convertToRatioLabel(Histogram histogram, int i) {
        return (i > 0 ? deQuantise(histogram.getUpperBoundAt(i - 1)) + " <  " : "")
                + " x <= " + deQuantise(histogram.getUpperBoundAt(i));
    }

    private long quantise(Double ratio) {
        return Math.round(ratio * QUANTISE_FACTOR);
    }

    private String deQuantise(long minVal) {
        return new BigDecimal(minVal).divide(new BigDecimal(QUANTISE_FACTOR)).toPlainString();
    }

    private String convertToLabel(Histogram histogram, int i) {
        return (i > 0 ? histogram.getUpperBoundAt(i - 1) + " <  " : "")
                + " x <= " + histogram.getUpperBoundAt(i);
    }

}
