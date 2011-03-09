package com.github.mread.turbulence4j.analysisapi;

import java.util.HashMap;
import java.util.Map;

public class CalculatorResults {

    private final Map<Calculator, CalculatorResult> results = new HashMap<Calculator, CalculatorResult>();

    public CalculatorResults(Calculator[] calculators) {
        for (Calculator calculator : calculators) {
            results.put(calculator, CalculatorResult.NEVER_RUN);
        }
    }

    public <T> CalculatorResult get(Calculator calculator) {
        return results.get(calculator);
    }

    public void put(Calculator calculator, CalculatorResult calculatorResult) {
        results.put(calculator, calculatorResult);
    }

}
