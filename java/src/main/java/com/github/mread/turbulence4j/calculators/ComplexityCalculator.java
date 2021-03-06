package com.github.mread.turbulence4j.calculators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;


import javancss.FunctionMetric;
import javancss.Javancss;

public class ComplexityCalculator implements Calculator<Map<String, ComplexityAndNcss>> {

    private final JavaFileFinder javaFileFinder;
    private final List<FileValue> results = new ArrayList<FileValue>();

    public ComplexityCalculator(JavaFileFinder javaFileFinder) {
        this.javaFileFinder = javaFileFinder;
    }

    @Override
    public void calculate() {
        results.clear();
        List<String> files = javaFileFinder.findAllJavaFiles();
        System.out.println("Java file finder found: " + files.size() + " files");
        for (String file : files) {
            File targetFile = new File(javaFileFinder.getBaseDir(), file);
            FileValue result = measureComplexity(file, targetFile);
            results.add(result);
        }
    }

    public FileValue measureComplexity(String filenameToUse, File targetFile) {
        Javancss javancss = new Javancss(targetFile);
        List<?> functionMetrics = javancss.getFunctionMetrics();
        FileValue result = new FileValue(filenameToUse, 0);
        result.value2 = javancss.getNcss();
        for (Object object : functionMetrics) {
            FunctionMetric functionMetric = (FunctionMetric) object;
            if (functionMetric.name.contains(".equals(") || functionMetric.name.contains(".hashCode()")) {
//                System.out.println("ignoring: " + functionMetric.name);
                continue;
            }
            result.value += functionMetric.ccn;
        }
        return result;
    }

    @Override
    public Map<String, ComplexityAndNcss> getResults() {
        Map<String, ComplexityAndNcss> mappedResults = new HashMap<String, ComplexityAndNcss>();
        for (FileValue fileValue : results) {
            mappedResults.put(fileValue.getFilename(),
                new ComplexityAndNcss(fileValue.getValue(), fileValue.getValue2()));
        }
        return mappedResults;
    }

    @Override
    public void setRange(String range) {
        // not interested in range
    }

}
