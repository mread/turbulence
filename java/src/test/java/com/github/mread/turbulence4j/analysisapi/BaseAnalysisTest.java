package com.github.mread.turbulence4j.analysisapi;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class BaseAnalysisTest {

    @Test
    public void passesRangeToAppropriateAnalysisParts() {

        final Calculator<?> calculator = mock(Calculator.class);
        final Output output = mock(Output.class);

        BaseAnalysis baseAnalysis = new BaseAnalysis() {
            @Override
            public void configure() {
                configureCalculators(calculator);
                configureOutputs(output);
            }
        };
        baseAnalysis.setRange("1..2");
        baseAnalysis.configure();
        baseAnalysis.run();

        verify(calculator).setRange("1..2");
        verify(output).setRange("1..2");
    }
}
