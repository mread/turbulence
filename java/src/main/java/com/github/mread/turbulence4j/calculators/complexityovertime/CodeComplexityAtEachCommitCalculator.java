package com.github.mread.turbulence4j.calculators.complexityovertime;

import java.io.File;
import java.util.List;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.git.GitAdapter;

public class CodeComplexityAtEachCommitCalculator implements Calculator<List<CommitTimeWithComplexity>> {

    private final File targetDirectory;
    private final GitAdapter gitAdapter;
    
    private List<CommitTimeWithComplexity> results;
    private String range;

    public CodeComplexityAtEachCommitCalculator(File targetDirectory, GitAdapter gitAdapter) {
        this.targetDirectory = targetDirectory;
        this.gitAdapter = gitAdapter;
    }

    @Override
    public void calculate() {
        
    }

    @Override
    public List<CommitTimeWithComplexity> getResults() {
        return results;
    }

    @Override
    public void setRange(String range) {
        this.range = range;
    }

}
