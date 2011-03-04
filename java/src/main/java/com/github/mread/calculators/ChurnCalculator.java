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
    private List<NameValue> result;

    public ChurnCalculator(File targetDirectory, JavaFileFinder javaFileFinder, GitAdapter gitAdapter) {
        this.targetDirectory = targetDirectory;
        this.javaFileFinder = javaFileFinder;
        this.gitAdapter = gitAdapter;
    }

    public int calculate() {
        BufferedReader log = gitAdapter.getLog(targetDirectory);
        result = excludingUninterestingFiles(groupUp(fileChurn(withoutNewlines(log))));
        if (result.size() == 0) {
            return 0;
        }
        return sumFrom(result).getValue();
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

    List<NameValue> fileChurn(List<String> input) {
        List<NameValue> result = new ArrayList<NameValue>();
        for (String line : input) {
            String[] split = line.split("\t");
            result.add(new NameValue(split[2], addsPlusDeletes(split)));
        }
        return result;
    }

    public List<NameValue> groupUp(List<NameValue> input) {
        List<NameValue> results = new ArrayList<NameValue>();

        Group<NameValue> groupedByName = group(input, by(on(NameValue.class).getName()));
        Set<String> names = groupedByName.keySet();
        for (String name : names) {
            NameValue result = new NameValue(name, 0);
            List<NameValue> values = groupedByName.find(name);
            for (int i = 0; i < values.size() - CHANGES_TO_EXCLUDE; i++) {
                result.value += values.get(i).value;
            }
            results.add(result);
        }
        return results;
    }

    List<NameValue> excludingUninterestingFiles(List<NameValue> groupUp) {
        List<File> filesOfInterest = javaFileFinder.findAllJavaFiles();
        List<String> fileNames = convert(filesOfInterest, intoNames());
        return filter(having(on(NameValue.class).getName(), isIn(fileNames)), groupUp);
    }

    public List<NameValue> getResult() {
        return result;
    }

    public static class NameValue {

        final String name;
        int value;

        public NameValue(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name + ": " + value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            NameValue other = (NameValue) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (value != other.value)
                return false;
            return true;
        }

    }

    private Converter<File, String> intoNames() {
        return new Converter<File, String>() {
            @Override
            public String convert(File from) {
                String relativePath = from.getPath();
                if (relativePath.startsWith("./")) {
                    relativePath = relativePath.substring(2);
                }
                return relativePath;
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
