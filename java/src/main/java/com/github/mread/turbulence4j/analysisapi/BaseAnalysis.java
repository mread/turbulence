package com.github.mread.turbulence4j.analysisapi;

public abstract class BaseAnalysis implements Analysis {

    private final Calculator<?>[] calculators;
    private final Transformer<?>[] transformers;
    private final Output[] outputs;
    private final CalculatorResults calculatorResults;
    private final TransformerResults transformerResults;

    public BaseAnalysis(Calculator<?>[] calculators, Transformer<?>[] transformers, Output[] outputs) {
        this.calculators = calculators;
        this.transformers = transformers;
        this.outputs = outputs;
        calculatorResults = new CalculatorResults(calculators);
        transformerResults = new TransformerResults(transformers);
    }

    @Override
    public final void run() {
        for (Calculator<?> calculator : calculators) {
            runCalculator(calculator);
        }
        for (Transformer<?> transformer : transformers) {
            runTransformer(transformer);
        }
        for (Output output : outputs) {
            runOutput(output);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void runCalculator(Calculator<T> calculator) {
        calculatorResults.put((Class<? extends Calculator<T>>) calculator.getClass(),
                calculator.run());
    }

    @SuppressWarnings("unchecked")
    private <T> void runTransformer(Transformer<T> transformer) {
        transformerResults.put((Class<? extends Transformer<T>>) transformer.getClass(),
                transformer.run(calculatorResults));
    }

    private void runOutput(Output output) {
        output.run(transformerResults);
    }

}