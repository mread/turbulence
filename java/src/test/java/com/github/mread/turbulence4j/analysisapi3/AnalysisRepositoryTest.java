package com.github.mread.turbulence4j.analysisapi3;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisRepositoryTest {

    @Mock
    private Analysis mockAnalysis;

    @Test
    public void canRegisterAnAnalysis() {
        AnalysisRepository repository = new AnalysisRepository();
        repository.register(mockAnalysis);

        verify(mockAnalysis).configureCalculators();
        verify(mockAnalysis).configureTransformers();
        verify(mockAnalysis).configureOutputs();

        assertThat(repository.isRegistered(mockAnalysis), equalTo(true));
    }

}
