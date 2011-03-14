package com.github.mread.turbulence4j.analysisapi3;

public class SimpleAnalysis extends BaseAnalysis {

    private Calculator calculator1;
    private Calculator calculator2;
    private Transformer transformer1;
    private Output output1;

    public SimpleAnalysis(Calculator calculator1, Calculator calculator2, Transformer transformer1, Output output1) {
        this.calculator1 = calculator1;
        this.calculator2 = calculator2;
        this.transformer1 = transformer1;
        this.output1 = output1;
    }

    @Override
    public void configureCalculators() {
        configureCalculator(calculator1, new Transformer[] { transformer1 });
        configureCalculator(calculator2, new Transformer[] { transformer1 });
    }

    @Override
    public void configureTransformers() {
        configureTransformer(transformer1, new Output[] { output1 });
    }

    @Override
    public void configureOutputs() {
        configureOutput(output1);
    }

}
