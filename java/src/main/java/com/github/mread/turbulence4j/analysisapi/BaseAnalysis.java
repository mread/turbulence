package com.github.mread.turbulence4j.analysisapi;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAnalysis implements Analysis {

    private List<Calculator<?>> calculators = new ArrayList<Calculator<?>>();
    private List<Transformer<?>> transformers = new ArrayList<Transformer<?>>();
    private List<Output> outputs = new ArrayList<Output>();

    protected void configureCalculators(Calculator<?>... calculators) {
        this.calculators.addAll(asList(calculators));
    }

    protected void configureTransformers(Transformer<?>... transformers) {
        this.transformers.addAll(asList(transformers));
    }

    protected void configureOutputs(Output... outputs) {
        this.outputs.addAll(asList(outputs));
    }

    @Override
    public final void run() {
        for (Calculator<?> calculator : calculators) {
            calculator.calculate();
        }
        for (Transformer<?> transformer : transformers) {
            transformer.transform();
        }
        for (Output output : outputs) {
            output.output();
        }
    }
}
