package com.github.mread.turbulence4j.calculators;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import com.github.mread.turbulence4j.calculators.ComplexityCalculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;

public class ComplexityCalculatorTest {

    private static final File WORKING_DIRECTORY = new File("src/test/java/com/github/mread/calculators/");

    @Test
    public void canMeasureTotalComplexityForAPackageDirectory() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(WORKING_DIRECTORY);
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(javaFileFinder);
        int score = complexityCalculator.calculate();
        assertThat("total complexity less than 1 - unlikely really", score, greaterThanOrEqualTo(1));
    }

    @Test
    public void canGetResults() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(WORKING_DIRECTORY);
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(javaFileFinder);
        complexityCalculator.calculate();
        Map<String, Integer> results = complexityCalculator.getResults();
        assertThat(results.size(), equalTo(3));
    }
}
