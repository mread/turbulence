package com.github.mread.turbulence4j.analysisapi3;

import java.util.HashSet;
import java.util.Set;

public class AnalysisRepository {

    private Set<Analysis> analyses = new HashSet<Analysis>();

    public void register(Analysis analysis) {

        analysis.configureCalculators();
        analysis.configureTransformers();
        analysis.configureOutputs();

        analyses.add(analysis);

    }

    public boolean isRegistered(Analysis analysis) {
        return analyses.contains(analysis);
    }

    public Set<Analysis> findAll() {
        return analyses;
    }

}
