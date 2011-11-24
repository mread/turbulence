package com.github.mread.turbulence4j.calculators;

public class ComplexityAndNcss {

    private final int complexity;
    private final int ncss;

    public ComplexityAndNcss(int complexity, int ncss) {
        this.complexity = complexity;
        this.ncss = ncss;
    }

    public int getComplexity() {
        return complexity;
    }

    public int getNcss() {
        return ncss;
    }

}
