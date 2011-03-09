package com.github.mread.turbulence4j.analysis;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisModulesSpikeTest {

    private final Calculator[] FAKE_CALCULATORS = new Calculator[] {
            new FakeCalculator1(),
            new FakeCalculator2() };
    private final Output[] FAKE_OUTPUTS = new Output[] {
            new FakeOutput1(),
            new FakeOutput2() };

    @Mock
    private Calculator mockCalculator1;
    @Mock
    private Calculator mockCalculator2;

    @Test
    public void anAnalysisRequiresCalculators() {
        new FakeAnalysis(FAKE_CALCULATORS, Output.NONE);
    }

    @Test
    public void anAnalysisRequiresOutputs() {
        new FakeAnalysis(Calculator.NONE, FAKE_OUTPUTS);
    }

    @Test
    public void whenExecutingAnAnalysisItRunsEachCalculator() {

        Analysis analysis = new FakeAnalysis(new Calculator[] { mockCalculator1, mockCalculator2 }, Output.NONE);
        analysis.run();
        verify(mockCalculator1).run();
        verify(mockCalculator2).run();
    }

    @Test
    public void gettingResultsRunsTheCalculatorIfNotAlreadyRun() {
        Analysis analysis = new FakeAnalysis(new Calculator[] { mockCalculator1, mockCalculator2 }, Output.NONE);
        analysis.getCalculatorResults(mockCalculator1);
        verify(mockCalculator1, times(1)).run();
        verify(mockCalculator2, never()).run();
    }

    @Test
    public void resultsFromACalculatorCanBeRetrievedWithoutRerunningTheCalculator() {
        Analysis analysis = new FakeAnalysis(new Calculator[] { mockCalculator1 }, Output.NONE);
        analysis.run();
        analysis.getCalculatorResults(mockCalculator1);
        verify(mockCalculator1, times(1)).run();
    }
}
