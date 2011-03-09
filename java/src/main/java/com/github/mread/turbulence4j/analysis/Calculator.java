package com.github.mread.turbulence4j.analysis;

public interface Calculator {

    Calculator[] NONE = new Calculator[] {};

    CalculatorResult run();

}
