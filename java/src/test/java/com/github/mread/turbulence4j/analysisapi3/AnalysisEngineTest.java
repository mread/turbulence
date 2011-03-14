package com.github.mread.turbulence4j.analysisapi3;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisEngineTest {

    @Mock
    private AnalysisRepository mockAnalysisRepository;

    @Mock
    private Analysis mockAnalysis;

    @Test
    public void canRunAnalyses() {

        AnalysisEngine engine = new AnalysisEngine(mockAnalysisRepository);
        when(mockAnalysisRepository.findAll()).thenReturn(setContaining(mockAnalysis));
        engine.runAll();

        verify(mockAnalysis).run();
    }

    private Set<Analysis> setContaining(Analysis analysis) {
        Set<Analysis> mockAnalysisInASet = new HashSet<Analysis>();
        mockAnalysisInASet.add(analysis);
        return mockAnalysisInASet;
    }
}
