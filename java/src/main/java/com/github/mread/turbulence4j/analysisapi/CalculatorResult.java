package com.github.mread.turbulence4j.analysisapi;

public interface CalculatorResult<T> {

    static class NeverRun implements CalculatorResult<String> {
        @Override
        public String getResult() {
            return "this calc has never been run";
        }
    }

    static CalculatorResult<String> NEVER_RUN = new NeverRun();

    T getResult();

}
