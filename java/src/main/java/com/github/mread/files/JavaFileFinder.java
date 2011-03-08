package com.github.mread.files;

import static org.apache.commons.io.FilenameUtils.concat;
import static org.apache.commons.io.FilenameUtils.separatorsToUnix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaFileFinder {

    private final File baseDir;

    public JavaFileFinder(File baseDir) {
        this.baseDir = baseDir;
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
            if (possibleDirectory.isDirectory()) {
                result.addAll(recurse(possibleDirectory,
                        separatorsToUnix(concat(prefix, filename))));
            }
        }
        return result;
    }

    public File getBaseDir() {
        return baseDir;
    }
}
