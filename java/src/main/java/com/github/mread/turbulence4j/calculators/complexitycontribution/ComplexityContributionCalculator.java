package com.github.mread.turbulence4j.calculators.complexitycontribution;

import java.io.File;
import java.util.List;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.calculators.AuthorValue;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ComplexityContributionCalculator implements Calculator<List<AuthorValue>> {

    private final File targetDirectory;
    public final GitAdapter gitAdapter;
    private List<AuthorValue> results;
    private String range = "";
	private final CommonProcessor processor;
	
    public ComplexityContributionCalculator(File targetDirectory, GitAdapter gitAdapter, CommonProcessor processor) {
        this.targetDirectory = targetDirectory;
        this.gitAdapter = gitAdapter;
		this.processor = processor;
    }

    @Override
    public void calculate() {
        gitAdapter.getGitVersion(targetDirectory);
        results = processor.process(gitAdapter.parseSha1s(getSha1s()));
    }

    @Override
    public List<AuthorValue> getResults() {
        return results;
    }

    List<String> getSha1s() {
        return gitAdapter.getLogOfSha1s(targetDirectory, range);
    }

    @Override
    public void setRange(String range) {
        this.range = range;
    }

}
