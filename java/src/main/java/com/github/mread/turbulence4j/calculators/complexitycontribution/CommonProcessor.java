package com.github.mread.turbulence4j.calculators.complexitycontribution;

import java.io.File;
import java.util.List;

import com.github.mread.turbulence4j.calculators.AuthorValue;
import com.github.mread.turbulence4j.calculators.ChurnCalculator;
import com.github.mread.turbulence4j.calculators.CommitParentAuthor;
import com.github.mread.turbulence4j.calculators.ComplexityCalculator;
import com.github.mread.turbulence4j.calculators.FileValue;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public abstract class CommonProcessor {

    private final GitAdapter gitAdapter;
    private final File targetDirectory;
    private final JavaFileFinder javaFileFinder;

    public CommonProcessor(GitAdapter gitAdapter, File targetDirectory, JavaFileFinder javaFileFinder) {
        this.gitAdapter = gitAdapter;
        this.targetDirectory = targetDirectory;
        this.javaFileFinder = javaFileFinder;
    }

    public abstract List<AuthorValue> process(List<CommitParentAuthor> commits);

    protected FileValue measureComplexity(String sha1, String targetFilename) {
        File file = getGitAdapter().doShow(getTargetDirectory(), sha1, targetFilename);
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(null);
        return complexityCalculator.measureComplexity(targetFilename, file);
    }

    protected int calculateNetChurn(CommitParentAuthor commitParentAuthor, List<String> modifiedFiles) {
        List<String> commitChurn = getGitAdapter().getCommitChurn(getTargetDirectory(), commitParentAuthor.getCommit());
        ChurnCalculator calculator = new ChurnCalculator(null, javaFileFinder, null);
        List<FileValue> results = calculator.churnByLogLine(commitChurn);
        int totalChurn = 0;
        for (FileValue fileValue : results) {
            if (matchesFileCriteria(fileValue.getFilename())) {
                totalChurn += fileValue.getValue();
            }
        }
        return totalChurn;
    }

    protected GitAdapter getGitAdapter() {
        return gitAdapter;
    }

    public File getTargetDirectory() {
        return targetDirectory;
    }

    protected void reportProgress(int number, int totalNumberOfCommits, int files) {
        System.out.print("Measuring commit complexity " + number + " of " + totalNumberOfCommits
                + " (" + files + " files) ");
    }

    protected void reportMicroProgress() {
        System.out.print(".");
    }

    protected void reportMicroProgressComplete() {
        System.out.println();
    }

    protected boolean matchesFileCriteria(String file) {
        return file.endsWith(".java") && !file.startsWith("src/test") && !file.startsWith("src/acceptance");
    }

    protected boolean meetsNetComplexityThreshold(long netFilesetComplexity) {
        // return Math.abs(netFilesetComplexity) < 100;
        return true;
    }

    protected List<String> filesInACommit(String sha1) {
        return getGitAdapter().getFilesForACommit(getTargetDirectory(), sha1);
    }

    protected int calculateNetComplexityDelta(CommitParentAuthor commitDetails, List<String> files) {
        int beforeComplexity = 0;
        int afterComplexity = 0;
        for (String file : files) {
            reportMicroProgress();
            if (matchesFileCriteria(file)) {
                beforeComplexity += measureComplexity(commitDetails.getParent(), file).getValue();
                afterComplexity += measureComplexity(commitDetails.getCommit(), file).getValue();
            }
        }
        reportMicroProgressComplete();
        return afterComplexity - beforeComplexity;
    }

}