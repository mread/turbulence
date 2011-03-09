package com.github.mread.turbulence4j.analysisapi;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.turbulence4j.analysis.closed.ClosedForReconfigurationAnalysis;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisModulesSpikeTest {

    private final Calculator[] FAKE_CALCULATORS = new Calculator[] {
            new FakeCalculator1(),
            new FakeCalculator2() };
    private final Transformer[] FAKE_TRANSFORMERS = new Transformer[] {
            new FakeTransformer1(),
            new FakeTransformer2() };
    private final Output[] FAKE_OUTPUTS = new Output[] {
            new FakeOutput1(),
            new FakeOutput2() };

    @Mock
    private Calculator mockCalculator1;
    @Mock
    private Calculator mockCalculator2;
    @Mock
    private Transformer mockTransformer1;
    @Mock
    private Transformer mockTransformer2;
    @Mock
    private Output mockOutput1;
    @Mock
    private Output mockOutput2;

    @Test
    public void anAnalysisCanBeClosedForReconfiguration() {
        new ClosedForReconfigurationAnalysis();
    }

    @Test
    public void anAnalysisRequiresCalculators() {
        new FakeAnalysis(FAKE_CALCULATORS, Transformer.NONE, Output.NONE);
    }

    @Test
    public void anAnalysisRequiresTransformers() {
        new FakeAnalysis(Calculator.NONE, FAKE_TRANSFORMERS, Output.NONE);
    }

    @Test
    public void anAnalysisRequiresOutputs() {
        new FakeAnalysis(Calculator.NONE, Transformer.NONE, FAKE_OUTPUTS);
    }

    @Test
    public void itRunsEachCalculator() {
        Analysis analysis = new FakeAnalysis(new Calculator[] { mockCalculator1, mockCalculator2 },
                Transformer.NONE,
                Output.NONE);
        analysis.run();
        verify(mockCalculator1).run();
        verify(mockCalculator2).run();
    }

    @Test
    public void itRunsEachTransformer() {
        Analysis analysis = new FakeAnalysis(Calculator.NONE,
                new Transformer[] { mockTransformer1, mockTransformer2 },
                Output.NONE);
        analysis.run();
        verify(mockTransformer1).run();
        verify(mockTransformer2).run();
    }

    @Test
    public void itRunsEachOutput() {
        Analysis analysis = new FakeAnalysis(Calculator.NONE,
                Transformer.NONE,
                new Output[] { mockOutput1, mockOutput2 });
        analysis.run();
        verify(mockOutput1).run();
        verify(mockOutput2).run();
    }

}
