package com.github.mread.javancss;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import javancss.FunctionMetric;
import javancss.Javancss;

import org.junit.Test;

import com.github.mread.calculators.ComplexityCalculator;

public class JavaNcssSpikeTest {

    @Test
    public void canAnalyseWithNcss() {
        Javancss javancss = new Javancss(new JavaFileFinder(new File(".")).findAllJavaFiles());
        System.out.println(javancss.printJavaNcss());
        List<?> functionMetrics = javancss.getFunctionMetrics();
        for (Object object : functionMetrics) {
            FunctionMetric functionMetric = (FunctionMetric) object;
            System.out.println(functionMetric.name + ": " + functionMetric.ccn);
        }
    }

    @Test
    public void canFindAllJavaFiles() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(new File("src/test/java/com/github/mread/javancss/"));
        List<File> files = javaFileFinder.findAllJavaFiles();
        assertThat("number of .java in this package should be 1", files.size(), equalTo(1));
    }

    @Test
    public void canMeasureTotalComplexityForAPackage() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(new File("src/test/java/com/github/mread/javancss/"));
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(javaFileFinder);
        int score = complexityCalculator.calculate();
        assertThat("total complexity less than 1 - unlikely", score, greaterThanOrEqualTo(1));
    }
}
