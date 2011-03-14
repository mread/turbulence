package com.github.mread.turbulence4j.analysisapi3;

public interface Transformer {

    void run();

    void setOutputs(Output[] outputs);

    void setResultOfCalculator(Calculator calculator, Object result);

}
