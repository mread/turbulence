package com.github.mread.turbulence4j.analysisapi;

import java.util.Map;

public interface Output {

    Output[] NONE = new Output[] {};

    void run(Map<Transformer, TransformerResult> transformerResults);

}
