package com.github.mread.turbulence4j.analysisapi;

import java.io.File;

import com.github.mread.turbulence4j.files.JavaFileFinder;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class BaseAnalysisTest {

    @Test
    public void passesRangeToAppropriateAnalysisParts() {

        final Calculator<?> calculator = mock(Calculator.class);
        final Output output = mock(Output.class);

        BaseAnalysis baseAnalysis = new BaseAnalysis() {
            @Override
            public void configure(File workingDirectory, JavaFileFinder fileFinder) {
                configureCalculators(calculator);
                configureOutputs(output);
            }
        };
        baseAnalysis.setRange("1..2");
        baseAnalysis.configure(new File("."), new JavaFileFinder(new File(".")));
        baseAnalysis.run();

        verify(calculator).setRange("1..2");
        verify(output).setRange("1..2");
    }
}
