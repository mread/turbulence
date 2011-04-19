package com.github.mread.turbulence4j.transformers;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static java.lang.Math.min;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.lambdaj.group.Group;

import com.github.mread.turbulence4j.analysisapi.Transformer;
import com.github.mread.turbulence4j.calculators.DistinctIssueCommitsPerPackageCalculator;
import com.github.mread.turbulence4j.calculators.Issue;
import com.github.mread.turbulence4j.calculators.IssuePackage;
import com.github.mread.turbulence4j.calculators.IssueTypeCalculator;

public class CountPackageMentionsTransformer implements Transformer<List<PackageChangesDefects>> {

    private final DistinctIssueCommitsPerPackageCalculator distinctIssueCommitsPerPackageCalculator;
    private final IssueTypeCalculator issueTypeCalculator;
    private int packageDepth = 6;
    private int topN = 30;
    private int changesThreshold = 1;
    private List<PackageChangesDefects> results;
    private Map<String, String> transformations = new HashMap<String, String>();

    public CountPackageMentionsTransformer(
            DistinctIssueCommitsPerPackageCalculator distinctIssueCommitsPerPackageCalculator,
            IssueTypeCalculator issueTypeCalculator) {

        this.distinctIssueCommitsPerPackageCalculator = distinctIssueCommitsPerPackageCalculator;
        this.issueTypeCalculator = issueTypeCalculator;
    }

    @Override
    public void transform() {
        Group<IssuePackage> groupedByPackage = group(distinctIssueCommitsPerPackageCalculator.getResults(),
                by(on(IssuePackage.class).getPackageName()));

        Map<String, PackageChangesDefects> resultsMap = new HashMap<String, PackageChangesDefects>();

        for (Group<IssuePackage> issuePackageGroup : groupedByPackage.subgroups()) {
            for (IssuePackage itemInPackage : issuePackageGroup.findAll()) {
                String originalPackageName = issuePackageGroup.first().getPackageName();
                String packageName = rollupPacknameToDepth(transformPackageName(originalPackageName), packageDepth);
                PackageChangesDefects existingEntry = resultsMap.get(packageName);
                if (existingEntry == null) {
                    existingEntry = new PackageChangesDefects(packageName, 0, 0);
                    resultsMap.put(packageName, existingEntry);
                }
                Issue issue = issueTypeCalculator.getResults().get(itemInPackage.getIssueKey());
                if (issue == null) {
                    System.err.println("Can't find issue: " + itemInPackage.getIssueKey());
                    continue;
                }
                if (!issue.getKey().startsWith("FRM")
                                        && !issue.getKey().startsWith("TEST")
                                        && !issue.getKey().startsWith("CHA")) {
                    System.err.println("Excluding issue: " + issue.getKey());
                    continue;
                }
                if (issue.isChange()) {
                    existingEntry.setChanges(existingEntry.getChanges() + 1);
                } else if (issue.isDefect()) {
                    existingEntry.setDefects(existingEntry.getDefects() + 1);
                }
            }
        }
        results = new ArrayList<PackageChangesDefects>(resultsMap.values());
        results = filter(having(on(PackageChangesDefects.class).getChanges(), greaterThan(changesThreshold)), results);
        results = sort(results, on(PackageChangesDefects.class).getSortAscendingValue());
        results = results.subList(0, min(topN, results.size()));
    }

    String rollupPacknameToDepth(String packageName, int depth) {
        if (depth == -1)
            return packageName;
        if (!packageName.contains("."))
            return packageName;

        int dotIndex = 0;
        for (int i = 0; i < depth; i++) {
            int nextDot = packageName.indexOf(".", dotIndex + 1);
            if (nextDot == -1) {
                dotIndex = packageName.length();
                break;
            }
            dotIndex = nextDot;
        }
        return packageName.substring(0, dotIndex);
    }

    @Override
    public List<PackageChangesDefects> getResults() {
        return results;
    }

    public Collection<IssuePackage> normaliseToDepth(Collection<IssuePackage> input) {
        return input;
    }

    public void setTransformations(Map<String, String> transformations) {
        this.transformations = transformations;
    }

    public String transformPackageName(String packageName) {
        String newPackageName = packageName;
        for (String pattern : transformations.keySet()) {
            Pattern patternCompiled = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patternCompiled.matcher(packageName);
            if (matcher.matches()) {
                newPackageName = matcher.replaceFirst(transformations.get(pattern));
                System.out.println(packageName + " -> " + newPackageName);
                break;
            }
        }
        return newPackageName;
    }

    public void setPackageDepth(int packageDepth) {
        this.packageDepth = packageDepth;
    }

    public void setTopN(int topN) {
        this.topN = topN;
    }

    public void setChangesThreshold(int changesThreshold) {
        this.changesThreshold = changesThreshold;
    }

}
