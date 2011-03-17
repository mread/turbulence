package com.github.mread.turbulence4j.analysisapi;

import java.util.Set;

public class AnalysisEngine {

    private final AnalysisRepository analysisRepository;
    private String range = "";

    public AnalysisEngine(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    public void runAll() {
        Set<Analysis> analyses = analysisRepository.findAll();
        for (Analysis analysis : analyses) {
            analysis.setRange(range);
            analysis.run();
        }
    }

    public AnalysisEngine forRange(String range) {
        this.range = range;
        return this;
    }

}
