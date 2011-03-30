package com.github.mread.turbulence4j.calculators;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectDistinct;
import static ch.lambdaj.Lambda.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.git.GitAdapter;

public class DistinctIssueCommitsPerPackageCalculator implements Calculator<Collection<IssuePackage>> {

    private static final String SRC_MAIN_JAVA = "src/main/java/";

    private final GitAdapter gitAdapter;
    private final File targetDirectory;
    private String range;

    private Collection<IssuePackage> results;

    public DistinctIssueCommitsPerPackageCalculator(GitAdapter gitAdapter, File targetDirectory) {
        this.gitAdapter = gitAdapter;
        this.targetDirectory = targetDirectory;
    }

    @Override
    public void calculate() {
        List<String> log = gitAdapter.getLogWithCommentsAndDirectories(targetDirectory, range);
        List<IssuePackage> processedRawLog = processRawLog(log);
        this.results = sort(issuesForPackageCommits(processedRawLog), on(IssuePackage.class).getPackageName());
    }

    @Override
    public Collection<IssuePackage> getResults() {
        return results;
    }

    @Override
    public void setRange(String range) {
        this.range = range;
    }

    public Collection<IssuePackage> issuesForPackageCommits(List<IssuePackage> issuePackageList) {
        return selectDistinct(issuePackageList);
    }

    public List<IssuePackage> processRawLog(List<String> input) {
        List<IssuePackage> output = new ArrayList<IssuePackage>();
        IssuePackage currentIssuePackage = null;
        for (String line : input) {
            if (line.isEmpty()) {
                continue;
            }
            if (matchesIssueKeyLine(line)) {
                currentIssuePackage = processIssueKeyLine(currentIssuePackage, line);
            } else {
                currentIssuePackage = processDirStatLine(currentIssuePackage, line, output);
            }
        }
        return output;
    }

    boolean matchesIssueKeyLine(String line) {
        return line.matches("[A-Z]+-[0-9]+:.*");
    }

    IssuePackage processIssueKeyLine(IssuePackage currentIssuePackage, String line) {
        if (currentIssuePackage == null) {
            currentIssuePackage = new IssuePackage();
        }
        currentIssuePackage.setIssueKey(line.substring(0, line.indexOf(":")));
        return currentIssuePackage;
    }

    private IssuePackage processDirStatLine(IssuePackage currentIssuePackage, String line, List<IssuePackage> output) {
        String packageName = extractPackageName(line);
        if (packageName != null) {
            currentIssuePackage.setPackageName(packageName);
            output.add(currentIssuePackage);
        }
        return new IssuePackage(currentIssuePackage.getIssueKey(), null);
    }

    String extractPackageName(String line) {
        int startOfPackagePart = line.indexOf(SRC_MAIN_JAVA) + SRC_MAIN_JAVA.length();
        if (startOfPackagePart == SRC_MAIN_JAVA.length() - 1) {
            return null;
        }
        String packageFragment = line.substring(startOfPackagePart, line.lastIndexOf("/"));
        return packageFragment.replace("/", ".");
    }

}
