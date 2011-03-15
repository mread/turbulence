package com.github.mread.turbulence4j.calculators;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

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

public class ChurnByAuthorCalculator implements Calculator<Map<AuthorFilenameKey, Integer>> {

    private static final int CHANGES_TO_EXCLUDE = 1;

    private final File workingDirectory;
    private final JavaFileFinder fileFinder;
    private final GitAdapter gitAdapter;
    private Map<AuthorFilenameKey, Integer> results;

    public ChurnByAuthorCalculator(File workingDirectory, JavaFileFinder fileFinder, GitAdapter gitAdapter) {
        this.workingDirectory = workingDirectory;
        this.fileFinder = fileFinder;
        this.gitAdapter = gitAdapter;
    }

    @Override
    public void calculate() {
        results = groupUp(filterOutFirstCommit(parseLines(gitAdapter.getLogWithAuthor(workingDirectory))));
    }

    @Override
    public Map<AuthorFilenameKey, Integer> getResults() {
        return results;
    }

    List<AuthorFileValue> parseLines(List<String> logLines) {
        List<AuthorFileValue> results = new ArrayList<AuthorFileValue>();
        for (String line : logLines) {
            String[] split = line.split("\t");
            results.add(new AuthorFileValue(split[0], addsPlusDeletes(split[1], split[2]), split[3]));
        }
        return results;
    }

    public List<AuthorFileValue> filterOutFirstCommit(List<AuthorFileValue> input) {
        List<AuthorFileValue> results = new ArrayList<AuthorFileValue>();
        Group<AuthorFileValue> groupedByFile = group(input, by(on(AuthorFileValue.class).getFilename()));
        Set<String> names = groupedByFile.keySet();
        for (String name : names) {
            List<AuthorFileValue> values = groupedByFile.find(name);
            for (int i = 0; i < values.size() - CHANGES_TO_EXCLUDE; i++) {
                results.add(values.get(i));
            }
        }
        return results;
    }

    Map<AuthorFilenameKey, Integer> groupUp(List<AuthorFileValue> lines) {
        Map<AuthorFilenameKey, Integer> results = new HashMap<AuthorFilenameKey, Integer>();
        for (AuthorFileValue line : lines) {
            AuthorFilenameKey key = keyOf(line.getAuthor(), line.getFilename());
            Integer existingValue = results.get(key);
            int value = calculateNewValue(line, existingValue);
            results.put(key, value);
        }
        return results;
    }

    private int calculateNewValue(AuthorFileValue line, Integer existingValue) {
        return (existingValue == null ? 0 : existingValue) + line.getChurn();
    }

    private int addsPlusDeletes(String addsString, String deletesString) {
        try {
            int adds = Integer.parseInt(addsString);
            int deletes = Integer.parseInt(deletesString);
            return adds + deletes;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    static AuthorFilenameKey keyOf(String author, String filename) {
        return new AuthorFilenameKey(author, filename);
    }

}
