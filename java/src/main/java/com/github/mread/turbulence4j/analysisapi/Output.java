package com.github.mread.turbulence4j.analysisapi;


public interface Output {

    Output[] NONE = new Output[] {};

    void run(TransformerResults transformerResults);

}
