package com.github.mread.turbulence4j.analysisapi;

public class FakeAnalysis extends BaseAnalysis {

    FakeAnalysis(Calculator<?>[] calculators, Transformer<?>[] transformers, Output[] outputs) {
        super(calculators, transformers, outputs);
    }

}
