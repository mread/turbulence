package com.github.mread.calculators;

import java.util.List;

import javancss.FunctionMetric;
import javancss.Javancss;

import com.github.mread.javancss.JavaFileFinder;

public class ComplexityCalculator {

    private final JavaFileFinder javaFileFinder;

    public ComplexityCalculator(JavaFileFinder javaFileFinder) {
        this.javaFileFinder = javaFileFinder;
    }

    public int calculate() {
        Javancss javancss = new Javancss(javaFileFinder.findAllJavaFiles());
        List<?> functionMetrics = javancss.getFunctionMetrics();
        int totalComplexity = 0;
        for (Object object : functionMetrics) {
            FunctionMetric functionMetric = (FunctionMetric) object;
            totalComplexity += functionMetric.ccn;
        }
        return totalComplexity;
    }
}
