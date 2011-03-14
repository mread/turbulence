package com.github.mread.turbulence4j.analysisapi;

import java.util.HashSet;
import java.util.Set;

public class AnalysisRepository {

    private Set<Analysis> analyses = new HashSet<Analysis>();

    public void register(Analysis analysis) {
        analysis.configure();
        analyses.add(analysis);
    }

    public boolean isRegistered(Analysis analysis) {
        return analyses.contains(analysis);
    }

    public Set<Analysis> findAll() {
        return analyses;
    }

}
