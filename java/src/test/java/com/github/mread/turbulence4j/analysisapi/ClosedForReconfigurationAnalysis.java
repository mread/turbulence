package com.github.mread.turbulence4j.analysisapi;


public class ClosedForReconfigurationAnalysis extends BaseAnalysis {

    /**
     * Constructor hides configuration details
     */
    public ClosedForReconfigurationAnalysis() {
        super(Calculator.NONE, Transformer.NONE, Output.NONE);
    }

}
