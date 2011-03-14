package com.github.mread.turbulence4j.analysisapi3;

public abstract class BaseCalculator implements Calculator {

    private Transformer[] transformers;

    @Override
    public final void setTransformers(Transformer[] transformers) {
        this.transformers = transformers;
    }

    @Override
    public final void run() {
        Object result = calculate();
        for (Transformer transformer : transformers) {
            transformer.setResultOfCalculator(this, result);
        }
    }

    protected abstract Object calculate();
}
