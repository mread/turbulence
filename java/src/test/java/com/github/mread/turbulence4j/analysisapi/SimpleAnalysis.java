package com.github.mread.turbulence4j.analysisapi;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.analysisapi.Transformer;

public class SimpleAnalysis extends BaseAnalysis {

    private Transformer<Integer> transformerA;

    public SimpleAnalysis() {

    }

    @Override
    public void configure() {
        Calculator<Integer> calculatorA = new CalculatorA();
        Calculator<Integer> calculatorB = new CalculatorB();
        transformerA = new TransformerA(calculatorA, calculatorB);
        Output outputA = new OutputA(transformerA);
        Output outputB = new OutputB(transformerA);

        configureCalculators(calculatorA, calculatorB);
        configureTransformers(transformerA);
        configureOutputs(outputA, outputB);
    }

    public Integer getResultOfTransformer() {
        return transformerA.getResults();
    }

}
