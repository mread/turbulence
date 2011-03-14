package com.github.mread.turbulence4j.analysisapi3;

public abstract class BaseTransformer implements Transformer {

    private Output[] outputs;

    @Override
    public final void setOutputs(Output[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public final void run() {
        Object result = transform();
        for (Output output : outputs) {
            output.setResultOfTransformer(this, result);
        }

    }

    public abstract Object transform();

}
