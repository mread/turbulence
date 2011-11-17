package com.github.mread.turbulence4j.analysisapi;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.github.mread.turbulence4j.files.JavaFileFinder;

public class AnalysisRepository {

    private Set<Analysis> analyses = new HashSet<Analysis>();
    private File workingDirectory;
    private JavaFileFinder fileFinder;

    public void register(Analysis analysis) {
        analysis.configure(workingDirectory, fileFinder);
        analyses.add(analysis);
    }

    public boolean isRegistered(Analysis analysis) {
        return analyses.contains(analysis);
    }

    public Set<Analysis> findAll() {
        return analyses;
    }

    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void setFileFinder(JavaFileFinder fileFinder) {
        this.fileFinder = fileFinder;
    }

}
