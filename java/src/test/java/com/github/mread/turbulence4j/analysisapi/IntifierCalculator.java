package com.github.mread.turbulence4j.analysisapi;

class IntifierCalculator implements Calculator<Integer> {
    private final int result;

    public IntifierCalculator(int result) {
        this.result = result;
    }

    @Override
    public CalculatorResult<Integer> run() {
        return new CalculatorResult<Integer>() {
            @Override
            public Integer getResult() {
                return result;
            }
        };
    }
}