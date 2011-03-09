package com.github.mread.turbulence4j.analysisapi;

public interface CalculatorResult {

    static CalculatorResult NEVER_RUN = new CalculatorResult() {
        @Override
        public Object getResult() {
            return null;
        }
    };

    Object getResult();

}
