package com.github.mread.turbulence4j.analysisapi;

public interface Calculator {

    Calculator[] NONE = new Calculator[] {};

    CalculatorResult run();

}
