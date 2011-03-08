package com.github.mread.calculators;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javancss.FunctionMetric;
import javancss.Javancss;

import com.github.mread.files.JavaFileFinder;

public class ComplexityCalculator {

    private final JavaFileFinder javaFileFinder;
    private List<FileValue> results = new ArrayList<FileValue>();

    public ComplexityCalculator(JavaFileFinder javaFileFinder) {
        this.javaFileFinder = javaFileFinder;
    }

    public int calculate() {
        List<File> files = javaFileFinder.findAllJavaFiles();
        int totalComplexity = 0;
        for (File file : files) {
            Javancss javancss = new Javancss(file);
            List<?> functionMetrics = javancss.getFunctionMetrics();
            FileValue result = new FileValue(file, 0);
            for (Object object : functionMetrics) {
                FunctionMetric functionMetric = (FunctionMetric) object;
                result.value += functionMetric.ccn;
                totalComplexity += functionMetric.ccn;
            }
            results.add(result);
        }
        return totalComplexity;
    }

    public List<FileValue> getResults() {
        Collections.sort(results);
        return results;
    }

}