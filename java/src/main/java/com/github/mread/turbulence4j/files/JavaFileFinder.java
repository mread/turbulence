package com.github.mread.turbulence4j.files;

import static org.apache.commons.io.FilenameUtils.concat;
import static org.apache.commons.io.FilenameUtils.separatorsToUnix;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.not;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaFileFinder {

    private static final String[] NO_EXCLUDES = new String[] {};
    private final File baseDir;
    private String[] excludes;

    public JavaFileFinder(File baseDir) {
        this(baseDir, NO_EXCLUDES);
    }

    public JavaFileFinder(File baseDir, String... excludes) {
        this.baseDir = baseDir;
        this.excludes = excludes;
    }

    public List<String> findAllJavaFiles() {
        if (!baseDir.isDirectory()) {
            return Collections.emptyList();
        }

        return findJavaFiles(baseDir);
    }

    private List<String> findJavaFiles(File dir) {
        return recurse(dir, "");
    }

    private List<String> recurse(File dir, String prefix) {
        List<String> result = new ArrayList<String>();
        for (String filename : dir.list()) {
            if (filename.endsWith(".java")) {
                result.add(separatorsToUnix(concat(prefix, filename)));
                continue;
            }
            File possibleDirectory = new File(dir, filename);
            if (possibleDirectory.isDirectory() && notExcludedByPrefix(prefix, filename)) {
                result.addAll(recurse(possibleDirectory,
                        separatorsToUnix(concat(prefix, filename))));
            }
        }
        return result;
    }

    boolean notExcludedByPrefix(String prefix, String filename) {
        if (!prefix.equals("")) {
            return true;
        }
        return not(hasItemInArray(filename)).matches(excludes);
    }

    public File getBaseDir() {
        return baseDir;
    }
}
