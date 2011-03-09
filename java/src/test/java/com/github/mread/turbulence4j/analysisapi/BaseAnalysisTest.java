package com.github.mread.turbulence4j.analysisapi;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseAnalysisTest {

    @Mock
    private Calculator mockCalculator1;

    @Test
    public void gettingResultsRunsTheCalculatorIfNotAlreadyRun() {
        Analysis analysis = new FakeAnalysis(new Calculator[] { mockCalculator1 },
                Transformer.NONE,
                Output.NONE);
        analysis.getCalculatorResults(mockCalculator1);
        verify(mockCalculator1, times(1)).run();
    }

    @Test
    public void resultsFromACalculatorCanBeRetrievedWithoutRerunningTheCalculator() {
        Analysis analysis = new FakeAnalysis(new Calculator[] { mockCalculator1 },
                Transformer.NONE,
                Output.NONE);
        analysis.run();
        analysis.getCalculatorResults(mockCalculator1);
        verify(mockCalculator1, times(1)).run();
    }

}
