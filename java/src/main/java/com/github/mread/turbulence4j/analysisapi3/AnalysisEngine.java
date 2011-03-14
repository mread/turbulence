package com.github.mread.turbulence4j.analysisapi3;

import java.util.Set;

public class AnalysisEngine {

    private final AnalysisRepository analysisRepository;

    public AnalysisEngine(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    public void runAll() {
        Set<Analysis> analyses = analysisRepository.findAll();
        for (Analysis analysis : analyses) {
            analysis.run();
        }
    }

}
