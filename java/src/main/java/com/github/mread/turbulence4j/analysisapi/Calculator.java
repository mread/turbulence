package com.github.mread.turbulence4j.analysisapi;

public interface Calculator<T> {

    Calculator<?>[] NONE = new Calculator<?>[] {};

    CalculatorResult<T> run();

}
