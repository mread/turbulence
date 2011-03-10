package com.github.mread.turbulence4j.calculators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javancss.FunctionMetric;
import javancss.Javancss;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.CalculatorResult;
import com.github.mread.turbulence4j.files.JavaFileFinder;

public class ComplexityCalculator implements Calculator<Map<String, Integer>> {

    private final JavaFileFinder javaFileFinder;
    private final List<FileValue> results = new ArrayList<FileValue>();

    public ComplexityCalculator(JavaFileFinder javaFileFinder) {
        this.javaFileFinder = javaFileFinder;
    }

    @Override
    public ComplexityCalculatorResult run() {
        calculate();
        return new ComplexityCalculatorResult(getResults());
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

    public class ComplexityCalculatorResult implements CalculatorResult<Map<String, Integer>> {

        private final Map<String, Integer> results;

        public ComplexityCalculatorResult(Map<String, Integer> results) {
            this.results = results;
        }

        @Override
        public Map<String, Integer> getResult() {
            return results;
        }

    }

}
