package com.github.mread.turbulence4j.analysisapi;

class StringifierCalculator implements Calculator<String> {

    private final String result;

    public StringifierCalculator(String result) {
        this.result = result;
    }

    @Override
    public CalculatorResult<String> run() {
        return new CalculatorResult<String>() {
            @Override
            public String getResult() {
                return result;
            }
        };
    }
}