package com.github.mread.turbulence4j.analysisapi;

public interface Transformer<T> {

    Transformer<?>[] NONE = new Transformer<?>[] {};

    TransformerResult<T> run(CalculatorResults calculatorResults);

}
