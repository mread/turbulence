package com.github.mread.calculators;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

import com.github.mread.files.JavaFileFinder;

public class ComplexityCalculatorTest {

    @Test
    public void canMeasureTotalComplexityForAPackageDirectory() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(new File("src/test/java/com/github/mread/javancss/"));
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(javaFileFinder);
        int score = complexityCalculator.calculate();
        assertThat("total complexity less than 1 - unlikely", score, greaterThanOrEqualTo(1));
    }

}
