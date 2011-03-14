package com.github.mread.turbulence4j.analysisapi;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.Transformer;

public class TransformerA implements Transformer<Integer> {

    private final Calculator<Integer> calculatorA;
    private final Calculator<Integer> calculatorB;

    public TransformerA(Calculator<Integer> calculatorA, Calculator<Integer> calculatorB) {
        this.calculatorA = calculatorA;
        this.calculatorB = calculatorB;
    }

    @Override
    public void transform() {

    }

    @Override
    public Integer getResults() {
        return calculatorA.getResults() + calculatorB.getResults();
    }
}
