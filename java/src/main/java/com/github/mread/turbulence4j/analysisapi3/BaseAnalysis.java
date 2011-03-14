package com.github.mread.turbulence4j.analysisapi3;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAnalysis implements Analysis {

    private List<Calculator> calculators = new ArrayList<Calculator>();
    private List<Transformer> transformers = new ArrayList<Transformer>();
    private List<Output> outputs = new ArrayList<Output>();

    public void configureCalculator(Calculator calculator, Transformer[] transformers) {
        calculators.add(calculator);
        calculator.setTransformers(transformers);
    }

    public void configureTransformer(Transformer transformer, Output[] outputs) {
        transformers.add(transformer);
        transformer.setOutputs(outputs);
    }

    public void configureOutput(Output output) {
        outputs.add(output);
    }

    @Override
    public void run() {
        for (Calculator calculator : calculators) {
            calculator.run();
        }
        for (Transformer transformer : transformers) {
            transformer.run();
        }
        for (Output output : outputs) {
            output.run();
        }
    }
}
