package com.github.mread.turbulence4j.calculators;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.isIn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.lambdaj.group.Group;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ChurnCalculator implements Calculator<Map<String, Integer>> {

    private static final int CHANGES_TO_EXCLUDE = 1;
    private final GitAdapter gitAdapter;
    private final JavaFileFinder javaFileFinder;
    private final File targetDirectory;

    public int churn;
    private List<FileValue> results;
    private String range = "";

    public ChurnCalculator(File targetDirectory, JavaFileFinder javaFileFinder, GitAdapter gitAdapter) {
        this.targetDirectory = targetDirectory;
        this.javaFileFinder = javaFileFinder;
        this.gitAdapter = gitAdapter;
    }

    @Override
    public void calculate() {
        List<String> log = gitAdapter.getLog(targetDirectory, range);
        results = excludingUninterestingFiles(groupUp(churnByLogLine(log)));
    }

    @Override
    public Map<String, Integer> getResults() {
        Map<String, Integer> mappedResults = new HashMap<String, Integer>();
        for (FileValue fileValue : results) {
            mappedResults.put(fileValue.getFilename(), fileValue.getValue());
        }
        return mappedResults;
    }

    List<FileValue> excludingUninterestingFiles(List<FileValue> groupUp) {
        List<String> fileNames = javaFileFinder.findAllJavaFiles();
        return filter(having(on(FileValue.class).getFilename(), isIn(fileNames)), groupUp);
    }

    public List<FileValue> groupUp(List<FileValue> input) {
        List<FileValue> results = new ArrayList<FileValue>();

        Group<FileValue> groupedByName = group(input, by(on(FileValue.class).getFilename()));
        Set<String> names = groupedByName.keySet();
        for (String name : names) {
            FileValue result = new FileValue(name, 0);
            List<FileValue> values = groupedByName.find(name);
            for (int i = 0; i < values.size() - CHANGES_TO_EXCLUDE; i++) {
                result.value += values.get(i).value;
            }
            results.add(result);
        }
        return results;
    }

    List<FileValue> churnByLogLine(List<String> input) {
        MoveAggregator moveAggregator = new MoveAggregator(input);
        List<FileValue> result = new ArrayList<FileValue>();
        for (String line : input) {
            String[] split = line.split("\t");
            String filename = moveAggregator.getUltimateName(split[2]);
            result.add(new FileValue(filename, addsPlusDeletes(split)));
        }
        return result;
    }

    private int addsPlusDeletes(String[] split) {
        try {
            int adds = Integer.parseInt(split[0]);
            int deletes = Integer.parseInt(split[1]);
            return adds + deletes;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    @Override
    public void setRange(String range) {
        this.range = range;
    }

}
