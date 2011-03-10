package com.github.mread.turbulence4j.analysisapi;

public class FakeCalculator implements Calculator<Object> {

    private boolean wasRun = false;

    @Override
    public CalculatorResult<Object> run() {
        wasRun = true;
        return null;
    }

    boolean wasRun() {
        return wasRun;
    }

}