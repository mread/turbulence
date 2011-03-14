package com.github.mread.turbulence4j.analysisapi3;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExampleAnalysisTest {

    @Mock
    private Calculator mockCalculator1;
    @Mock
    private Calculator mockCalculator2;
    @Mock
    private Transformer mockTransformer1;
    @Mock
    private Output mockOutput1;

    private SimpleAnalysis analysis;

    @Before
    public void configure() {
        analysis = new SimpleAnalysis(mockCalculator1, mockCalculator2, mockTransformer1, mockOutput1);

        analysis.configureCalculators();
        analysis.configureTransformers();
        analysis.configureOutputs();
    }

    @Test
    public void managesWiring() {
        verify(mockCalculator1).setTransformers(new Transformer[] { mockTransformer1 });
        verify(mockCalculator2).setTransformers(new Transformer[] { mockTransformer1 });
        verify(mockTransformer1).setOutputs(new Output[] { mockOutput1 });
    }

    @Test
    public void runsEachPartOfAnAnalysis() {

        analysis.run();

        verify(mockCalculator1).run();
        verify(mockCalculator2).run();
        verify(mockTransformer1).run();
        verify(mockOutput1).run();

    }

}
