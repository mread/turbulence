package com.github.mread.javancss;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaFileFinder {

    private final File baseDir;

    public JavaFileFinder(File baseDir) {
        this.baseDir = baseDir;
    }

    public List<File> findAllJavaFiles() {
        if (!baseDir.isDirectory()) {
            return Collections.emptyList();
        }

        return findJavaFiles(baseDir);
    }

    private List<File> findJavaFiles(File dir) {
        List<File> result = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                result.addAll(findJavaFiles(file));
            }
            if (file.getName().endsWith(".java")) {
                result.add(file);
            }
        }
        return result;
    }
}
