package com.github.mread.turbulence4j.analysisapi;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAnalysis implements Analysis {

    private List<Calculator<?>> calculators = new ArrayList<Calculator<?>>();
    private List<Transformer<?>> transformers = new ArrayList<Transformer<?>>();
    private List<Output> outputs = new ArrayList<Output>();
    private String range;

    protected final void configureCalculators(Calculator<?>... calculators) {
        this.calculators.addAll(asList(calculators));
    }

    protected final void configureTransformers(Transformer<?>... transformers) {
        this.transformers.addAll(asList(transformers));
    }

    protected final void configureOutputs(Output... outputs) {
        this.outputs.addAll(asList(outputs));
    }

    @Override
    public final void setRange(String range) {
        this.range = range;
    }

    @Override
    public final void run() {
        for (Calculator<?> calculator : calculators) {
            calculator.setRange(range);
            calculator.calculate();
        }
        for (Transformer<?> transformer : transformers) {
            transformer.transform();
        }
        for (Output output : outputs) {
            output.setRange(range);
            output.output();
        }
    }
}
