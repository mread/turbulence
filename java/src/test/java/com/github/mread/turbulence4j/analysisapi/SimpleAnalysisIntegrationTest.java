package com.github.mread.turbulence4j.analysisapi;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleAnalysisIntegrationTest {

    private SimpleAnalysis analysis;

    @Before
    public void configure() {
        analysis = new SimpleAnalysis();
        analysis.configure();
    }

    @Test
    public void runsCalculatorsAndTransformer() {
        analysis.run();
        assertThat(analysis.getResultOfTransformer(), equalTo(3));
    }

}
