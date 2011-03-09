package com.github.mread.turbulence4j.analysis.closed;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.analysisapi.Transformer;

public class ClosedForReconfigurationAnalysis extends BaseAnalysis {

    /**
     * Constructor hides configuration details
     */
    public ClosedForReconfigurationAnalysis() {
        super(Calculator.NONE, Transformer.NONE, Output.NONE);
    }

}
