package com.github.mread.calculators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javancss.FunctionMetric;
import javancss.Javancss;

import com.github.mread.files.JavaFileFinder;

public class ComplexityCalculator {

    private final JavaFileFinder javaFileFinder;
    private final List<FileValue> results = new ArrayList<FileValue>();

    public ComplexityCalculator(JavaFileFinder javaFileFinder) {
        this.javaFileFinder = javaFileFinder;
    }

    public int calculate() {
        List<String> files = javaFileFinder.findAllJavaFiles();
        int totalComplexity = 0;
        for (String file : files) {
            Javancss javancss = new Javancss(new File(javaFileFinder.getBaseDir(), file));
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

    public Map<String, Integer> getResults() {
        Map<String, Integer> mappedResults = new HashMap<String, Integer>();
        for (FileValue fileValue : results) {
            mappedResults.put(fileValue.getFilename(), fileValue.getValue());
        }
        return mappedResults;
    }

}
