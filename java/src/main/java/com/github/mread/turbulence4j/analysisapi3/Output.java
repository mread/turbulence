package com.github.mread.turbulence4j.analysisapi3;

public interface Output {

    void run();

    void setResultOfTransformer(Transformer transformer, Object result);

}
