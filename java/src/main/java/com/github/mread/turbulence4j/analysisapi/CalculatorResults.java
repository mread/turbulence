package com.github.mread.turbulence4j.analysisapi;

import java.util.HashMap;
import java.util.Map;

public class CalculatorResults {

    @SuppressWarnings("rawtypes")
    private final Map<Class, CalculatorResult> results = new HashMap<Class, CalculatorResult>();

    public CalculatorResults(Calculator<?>[] calculators) {
        for (Calculator<?> calculator : calculators) {
            results.put(calculator.getClass(), CalculatorResult.NEVER_RUN);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> CalculatorResult<T> get(Class<? extends Calculator<T>> calculatorClass) {
        CalculatorResult<T> calculatorResult = results.get(calculatorClass);
        if (calculatorResult == null) {
            throw new RuntimeException("Calculator not configured for this analysis: "
                    + calculatorClass.getSimpleName());
        }
        return calculatorResult;
    }

    public <T> void put(Class<? extends Calculator<T>> calculatorClass, CalculatorResult<T> calculatorResult) {
        results.put(calculatorClass, calculatorResult);
    }

}
