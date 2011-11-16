package com.github.mread.turbulence4j.calculators.complexitycontribution;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mread.turbulence4j.calculators.AuthorValue;
import com.github.mread.turbulence4j.calculators.CommitParentAuthor;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ComplexityContributionProcessor extends CommonProcessor {

    public ComplexityContributionProcessor(GitAdapter gitAdapter, File targetDirectory, JavaFileFinder javaFileFinder) {
        super(gitAdapter, targetDirectory, javaFileFinder);
    }

    public List<AuthorValue> process(List<CommitParentAuthor> commits) {
        int totalNumberOfCommits = commits.size();
        int commitProgress = 0;
        Map<String, Long> output = new HashMap<String, Long>();
        for (CommitParentAuthor commitParentAuthor : commits) {
            List<String> filesInACommit = filesInACommit(commitParentAuthor.getCommit());
            reportProgress(++commitProgress, totalNumberOfCommits, filesInACommit.size());
            if (filesInACommit.size() > 200) {
                System.out.println("Ignoring large commit: " + commitParentAuthor.getCommit());
                continue;
            }
            long netFilesetComplexity = calculateNetComplexityDelta(commitParentAuthor, filesInACommit);
            System.out.println(commitParentAuthor.getAuthor() + " - " + commitParentAuthor.getCommit() + " - " + netFilesetComplexity);

            if (netFilesetComplexity != 0 && meetsNetComplexityThreshold(netFilesetComplexity)) {

                Long runningTotalForAuthor = output.get(commitParentAuthor.getAuthor());
                if (runningTotalForAuthor == null) {
                    runningTotalForAuthor = 0L;
                }

                output.put(commitParentAuthor.getAuthor(), runningTotalForAuthor + netFilesetComplexity);
                if (netFilesetComplexity > 20) {
                    System.out.println(netFilesetComplexity + " - " + commitParentAuthor);
                }
            }
        }
        return doSort(output);
    }

    List<AuthorValue> doSort(Map<String, Long> input) {
        List<AuthorValue> results = new ArrayList<AuthorValue>();
        for (String author : input.keySet()) {
            results.add(new AuthorValue(author, input.get(author)));
        }
        return sort(results, on(AuthorValue.class).getValue());
    }

}
