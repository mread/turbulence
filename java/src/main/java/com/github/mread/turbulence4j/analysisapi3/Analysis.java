package com.github.mread.turbulence4j.analysisapi3;

public interface Analysis {

    void configureCalculators();

    void configureTransformers();

    void configureOutputs();

    void run();
}
