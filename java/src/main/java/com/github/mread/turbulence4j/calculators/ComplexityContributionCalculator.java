package com.github.mread.turbulence4j.calculators;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ComplexityContributionCalculator implements Calculator<List<AuthorValue>> {

    private final File targetDirectory;
    private final JavaFileFinder javaFileFinder;
    private final GitAdapter gitAdapter;
    private List<AuthorValue> results;
    private String range = "";

    public ComplexityContributionCalculator(File targetDirectory, JavaFileFinder javaFileFinder, GitAdapter gitAdapter) {
        this.targetDirectory = targetDirectory;
        this.javaFileFinder = javaFileFinder;
        this.gitAdapter = gitAdapter;
    }

    @Override
    public void calculate() {
        gitAdapter.getGitVersion(targetDirectory);
        results = convertToSortedList(iterateCommitsGroupingComplexityByAuthor(structureSha1s(getSha1s())));
    }

    List<AuthorValue> convertToSortedList(Map<String, Integer> input) {
        List<AuthorValue> results = new ArrayList<AuthorValue>();
        for (String author : input.keySet()) {
            results.add(new AuthorValue(author, input.get(author)));
        }

        return sort(results, on(AuthorValue.class).getValue());
    }

    private Map<String, Integer> iterateCommitsGroupingComplexityByAuthor(List<CommitParentAuthor> commits) {
        int totalNumberOfCommits = commits.size();
        Map<String, Integer> output = new HashMap<String, Integer>();
        int commitProgress = 0;
        for (CommitParentAuthor commitParentAuthor : commits) {
            List<String> filesInACommit = filesInACommit(commitParentAuthor.getCommit());
            reportProgress(++commitProgress, totalNumberOfCommits, filesInACommit.size());
            if (filesInACommit.size() > 100) {
                System.out.println("Large commit: " + commitParentAuthor.getCommit());
                continue;
            }
            int beforeComplexity = 0;
            int afterComplexity = 0;
            for (String file : filesInACommit) {
                reportMicroProgress();
                if (file.endsWith(".java")) {
                    beforeComplexity += measureComplexity(commitParentAuthor.getParent(), file).getValue();
                    afterComplexity += measureComplexity(commitParentAuthor.getCommit(), file).getValue();
                }
            }
            reportMicroProgressComplete();
            int netFilesetComplexity = afterComplexity - beforeComplexity;
            if (netFilesetComplexity != 0 && meetsNetComplexityThreshold(netFilesetComplexity)) {
                Integer runningTotalForAuthor = output.get(commitParentAuthor.getAuthor());
                if (runningTotalForAuthor == null) {
                    runningTotalForAuthor = 0;
                }
                output.put(commitParentAuthor.getAuthor(), runningTotalForAuthor + netFilesetComplexity);
            }
        }
        return output;
    }

    private boolean meetsNetComplexityThreshold(int netFilesetComplexity) {
        return Math.abs(netFilesetComplexity) < 100;
    }

    @Override
    public List<AuthorValue> getResults() {
        return results;
    }

    List<String> getSha1s() {
        return gitAdapter.getLogOfSha1s(targetDirectory, range);
    }

    List<CommitParentAuthor> structureSha1s(List<String> input) {
        List<CommitParentAuthor> results = new ArrayList<CommitParentAuthor>();
        for (String line : input) {
            String[] split = line.split("\\|");
            if (split.length == 3) {
                results.add(new CommitParentAuthor(split[0], split[1], split[2]));
            }
        }
        return results;
    }

    List<String> filesInACommit(String sha1) {
        return gitAdapter.getFilesForACommit(targetDirectory, sha1);
    }

    public FileValue measureComplexity(String sha1, String targetFilename) {
        File file = gitAdapter.doShow(targetDirectory, sha1, targetFilename);
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(null);
        return complexityCalculator.measureComplexity(targetFilename, file);
    }

    private void reportProgress(int number, int totalNumberOfCommits, int files) {
        System.out.print("Measuring commit complexity " + number + " of " + totalNumberOfCommits
                + " (" + files + " files) ");
    }

    private void reportMicroProgress() {
        System.out.print(".");
    }

    private void reportMicroProgressComplete() {
        System.out.println();
    }

    @Override
    public void setRange(String range) {
        this.range = range;
    }

}
