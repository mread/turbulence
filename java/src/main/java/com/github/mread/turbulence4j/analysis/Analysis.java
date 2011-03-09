package com.github.mread.turbulence4j.analysis;

public interface Analysis {

    void run();

    CalculatorResult getCalculatorResults(Calculator calculator);

}
