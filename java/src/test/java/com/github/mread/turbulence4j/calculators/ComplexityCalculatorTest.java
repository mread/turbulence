package com.github.mread.turbulence4j.calculators;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

import com.github.mread.turbulence4j.files.JavaFileFinder;

public class ComplexityCalculatorTest {

    private static final File WORKING_DIRECTORY = new File("src/test/java/com/github/mread/turbulence4j/calculators/");

    @Test
    public void canMeasureTotalComplexityForAPackageDirectory() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(WORKING_DIRECTORY);
        ComplexityCalculator complexityCalculator = new ComplexityCalculator(javaFileFinder);
        complexityCalculator.calculate();
        assertThat(complexityCalculator.getResults().size(), greaterThan(0));
    }

}
