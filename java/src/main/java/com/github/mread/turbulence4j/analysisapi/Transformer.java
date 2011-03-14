package com.github.mread.turbulence4j.analysisapi;

public interface Transformer<R> {

    void transform();

    R getResults();

}
