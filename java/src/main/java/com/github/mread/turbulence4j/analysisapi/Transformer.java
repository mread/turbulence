package com.github.mread.turbulence4j.analysisapi;


public interface Transformer {

    Transformer[] NONE = new Transformer[] {};

    TransformerResult run(CalculatorResults calculatorResults);

}