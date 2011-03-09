package com.github.mread.turbulence4j.analysisapi;


public class FakeConfiguredAnalysis extends BaseAnalysis {

    public FakeConfiguredAnalysis() {
        super(new Calculator[] { new FakeCalculator1() },
                new Transformer[] { new FakeTransformer1() },
                new Output[] { new FakeOutput1() });

    }

}
