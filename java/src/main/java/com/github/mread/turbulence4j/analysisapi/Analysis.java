package com.github.mread.turbulence4j.analysisapi;

public interface Analysis {

    void run();

    CalculatorResult getCalculatorResults(Calculator calculator);

}
