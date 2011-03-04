package com.github.mread.calculators;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sumFrom;
import static org.hamcrest.Matchers.isIn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ch.lambdaj.function.convert.Converter;
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
        BufferedReader log = gitAdapter.getLog(targetDirectory);
        results = excludingUninterestingFiles(groupUp(fileChurn(withoutNewlines(log))));
        if (results.size() == 0) {
            return 0;
        }
        return sumFrom(results).getValue();
    }

    List<String> withoutNewlines(BufferedReader reader) {

        List<String> result = new ArrayList<String>();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    result.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    List<FileValue> fileChurn(List<String> input) {
        List<FileValue> result = new ArrayList<FileValue>();
        for (String line : input) {
            String[] split = line.split("\t");
            result.add(new FileValue(split[2], addsPlusDeletes(split)));
        }
        return result;
    }

    public List<FileValue> groupUp(List<FileValue> input) {
        List<FileValue> results = new ArrayList<FileValue>();

        Group<FileValue> groupedByName = group(input, by(on(FileValue.class).getFile()));
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

    List<FileValue> excludingUninterestingFiles(List<FileValue> groupUp) {
        List<String> fileNames = convert(javaFileFinder.findAllJavaFiles(), intoNames());
        return filter(having(on(FileValue.class).getFile(), isIn(fileNames)), groupUp);
    }

    public List<FileValue> getResults() {
        Collections.sort(results);
        return results;
    }

    private Converter<File, String> intoNames() {
        return new Converter<File, String>() {
            @Override
            public String convert(File from) {
                try {
                    return from.getCanonicalPath();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
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
