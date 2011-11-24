package com.github.mread.turbulence4j.calculators.complexityovertime;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.calculators.CommitParentAuthorTimestamp;
import com.github.mread.turbulence4j.calculators.ComplexityAndNcss;
import com.github.mread.turbulence4j.calculators.ComplexityCalculator;
import com.github.mread.turbulence4j.git.GitAdapter;

public class CodeComplexityAtEachCommitCalculator implements Calculator<List<CommitTimeWithComplexityAndNcss>> {

    private final ComplexityCalculator complexityCalculator;
    private final File targetDirectory;
    private final GitAdapter gitAdapter;

    private final DateFormat formatter = DateFormat.getDateInstance();
    
    private List<CommitTimeWithComplexityAndNcss> results;
    private String range;

    public CodeComplexityAtEachCommitCalculator(ComplexityCalculator complexityCalculator, File targetDirectory,
            GitAdapter gitAdapter) {
        this.complexityCalculator = complexityCalculator;
        this.targetDirectory = targetDirectory;
        this.gitAdapter = gitAdapter;
    }

    @Override
    public void calculate() {
        List<CommitParentAuthorTimestamp> commits = gitAdapter.parseSha1s(getAllCommitsInRange());
        int analyseEvery = Math.max(commits.size() / 10, 1);
        System.out.println("Analysing every " + analyseEvery + " commits approximately");
        try {
            results = new ArrayList<CommitTimeWithComplexityAndNcss>();
            for (int i = 0; i < commits.size(); i++) {
                if (i % analyseEvery == 0) {
                    calculateAndStoreTotalComplexityAtCommit(commits.get(i));
                }
            }
        } finally {
            gitAdapter.checkout(targetDirectory, "ORIG_HEAD");
        }
    }

    private void calculateAndStoreTotalComplexityAtCommit(CommitParentAuthorTimestamp commit) {
        gitAdapter.checkout(targetDirectory, commit.getCommit());
        complexityCalculator.calculate();
        Map<String, ComplexityAndNcss> complexities = complexityCalculator.getResults();
        int totalComplexity = 0;
        int totalNcss = 0;
        for (ComplexityAndNcss complexityAndNcss : complexities.values()) {
            totalComplexity += complexityAndNcss.getComplexity();
            totalNcss += complexityAndNcss.getNcss();
        }
        results.add(new CommitTimeWithComplexityAndNcss(commit.getTimestamp(), totalComplexity, totalNcss));
        System.out.println(formatter.format(new Date(commit.getTimestamp() * 1000)) + "," + totalComplexity);
    }

    List<String> getAllCommitsInRange() {
        List<String> commits = gitAdapter.getLogOfSha1s(targetDirectory, range);
        Collections.reverse(commits);
        return commits;
    }

    @Override
    public List<CommitTimeWithComplexityAndNcss> getResults() {
        return results;
    }

    @Override
    public void setRange(String range) {
        this.range = range;
    }

}
