package com.github.mread.turbulence4j.calculators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ChurnByAuthorCalculator implements Calculator<Map<AuthorFilenameKey, Integer>> {

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
        List<String> logWithAuthor = gitAdapter.getLogWithAuthor(workingDirectory);
        List<String[]> parsed = parseLines(logWithAuthor);
        results = groupUp(parsed);
    }

    @Override
    public Map<AuthorFilenameKey, Integer> getResults() {
        return results;
    }

    List<String[]> parseLines(List<String> logLines) {
        List<String[]> results = new ArrayList<String[]>();
        for (String line : logLines) {
            results.add(line.split("\t"));
        }
        return results;
    }

    Map<AuthorFilenameKey, Integer> groupUp(List<String[]> lines) {
        Map<AuthorFilenameKey, Integer> results = new HashMap<AuthorFilenameKey, Integer>();
        for (String[] line : lines) {
            AuthorFilenameKey key = keyOf(line[0], line[3]);
            Integer existingValue = results.get(key);
            int value = calculateNewValue(line, existingValue);
            results.put(key, value);
        }
        return results;
    }

    private int calculateNewValue(String[] line, Integer existingValue) {
        return (existingValue == null ? 0 : existingValue)
                + addsPlusDeletes(line[1], line[2]);
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
