package com.github.mread.turbulence4j.analysis;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseAnalysis implements Analysis {

    protected final Calculator[] calculators;
    private Map<Calculator, CalculatorResult> calculatorResults;

    public BaseAnalysis(Calculator[] calculators, Output[] outputs) {
        this.calculators = calculators;
        initialiseCalculatorResults();
    }

    protected void initialiseCalculatorResults() {
        calculatorResults = new HashMap<Calculator, CalculatorResult>();
        for (Calculator calculator : calculators) {
            calculatorResults.put(calculator, CalculatorResult.NEVER_RUN);
        }
    }

    @Override
    public void run() {
        for (Calculator calculator : calculators) {
            runCalculator(calculator);
        }
    }

    @Override
    public CalculatorResult getCalculatorResults(Calculator calculator) {
        if (calculatorResults.get(calculator) == CalculatorResult.NEVER_RUN) {
            runCalculator(calculator);
        }
        return calculatorResults.get(calculator);
    }

    private void runCalculator(Calculator calculator) {
        calculatorResults.put(calculator, calculator.run());
    }

}