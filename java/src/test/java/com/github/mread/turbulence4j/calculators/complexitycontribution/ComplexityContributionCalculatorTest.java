package com.github.mread.turbulence4j.calculators.complexitycontribution;

import java.io.File;

import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ComplexityContributionCalculatorTest {

    @Mock
    private GitAdapter mockGitAdapter;
    @Mock
    private JavaFileFinder mockJavaFileFinder;
    private ComplexityContributionCalculator calculator;

    @Before
    public void setup() {
        File targetDirectory = new File(".");
        calculator = new ComplexityContributionCalculator(targetDirectory, mockGitAdapter, new ComplexityContributionProcessor(mockGitAdapter, targetDirectory, mockJavaFileFinder));
    }

    @Test
    public void getsAllTheCommits() {
        calculator.getSha1s();
        verify(mockGitAdapter).getLogOfSha1s(new File("."), "");
    }

}
