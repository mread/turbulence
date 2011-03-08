package com.github.mread.calculators;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sumFrom;
import static org.hamcrest.Matchers.isIn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.lambdaj.group.Group;

import com.github.mread.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ChurnCalculator {

    private static final int CHANGES_TO_EXCLUDE = 1;
    private final GitAdapter gitAdapter;
    private final JavaFileFinder javaFileFinder;
    private final File targetDirectory;

    public int churn;
    private List<FileValue> results;

    public ChurnCalculator(File targetDirectory, JavaFileFinder javaFileFinder, GitAdapter gitAdapter) {
        this.targetDirectory = targetDirectory;
        this.javaFileFinder = javaFileFinder;
        this.gitAdapter = gitAdapter;
    }

    public int calculate() {
        List<String> log = gitAdapter.getLog(targetDirectory);
        results = excludingUninterestingFiles(groupUp(churnByLogLine(log)));
        if (results.size() == 0) {
            return 0;
        }
        return sumFrom(results).getValue();
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
        List<FileValue> result = new ArrayList<FileValue>();
        for (String line : input) {
            String[] split = line.split("\t");
            result.add(generateFileValue(split));
        }
        return result;
    }

    private FileValue generateFileValue(String[] split) {
        String filePart = split[2];
        if (filePart.contains(" => ")) {
            FileValue fileValue = new FileValue(getMoves(filePart)[1], addsPlusDeletes(split));
            fileValue.addAlternative(getMoves(filePart)[0]);
            return fileValue;
        }
        return new FileValue(filePart, addsPlusDeletes(split));
    }

    String[] getMoves(String filePart) {
        int firstCurly = filePart.indexOf("{");
        int newPath = filePart.indexOf("=> ", firstCurly) + 3;
        int lastCurly = filePart.indexOf("}", newPath);

        return new String[] {
                filePart.substring(0, firstCurly)
                        + filePart.substring(firstCurly + 1, newPath - 4)
                        + filePart.substring(lastCurly + 1),
                filePart.substring(0, firstCurly)
                        + filePart.substring(newPath, lastCurly)
                        + filePart.substring(lastCurly + 1) };
    }

    public Map<String, Integer> getResults() {
        Map<String, Integer> mappedResults = new HashMap<String, Integer>();
        for (FileValue fileValue : results) {
            mappedResults.put(fileValue.getFilename(), fileValue.getValue());
        }
        return mappedResults;
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

}
