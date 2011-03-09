package com.github.mread.turbulence4j.analysisapi;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseAnalysis implements Analysis {

    private final Calculator[] calculators;
    private final Transformer[] transformers;
    private final Output[] outputs;
    private Map<Calculator, CalculatorResult> calculatorResults;

    public BaseAnalysis(Calculator[] calculators, Transformer[] transformers, Output[] outputs) {
        this.calculators = calculators;
        this.transformers = transformers;
        this.outputs = outputs;
        initialiseCalculatorResults();
    }

    @Override
    public final void run() {
        for (Calculator calculator : calculators) {
            runCalculator(calculator);
        }
        for (Transformer transformer : transformers) {
            runTransformer(transformer);
        }
        for (Output output : outputs) {
            runOutput(output);
        }
    }

    @Override
    public final CalculatorResult getCalculatorResults(Calculator calculator) {
        if (calculatorResults.get(calculator) == CalculatorResult.NEVER_RUN) {
            runCalculator(calculator);
        }
        return calculatorResults.get(calculator);
    }

    private void initialiseCalculatorResults() {
        calculatorResults = new HashMap<Calculator, CalculatorResult>();
        for (Calculator calculator : calculators) {
            calculatorResults.put(calculator, CalculatorResult.NEVER_RUN);
        }
    }

    private void runCalculator(Calculator calculator) {
        calculatorResults.put(calculator, calculator.run());
    }

    private void runTransformer(Transformer transformer) {
        transformer.run();
    }

    private void runOutput(Output output) {
        output.run();
    }

}