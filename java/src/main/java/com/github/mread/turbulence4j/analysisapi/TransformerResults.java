package com.github.mread.turbulence4j.analysisapi;

import java.util.HashMap;
import java.util.Map;

public class TransformerResults {

    @SuppressWarnings("rawtypes")
    private final Map<Class, TransformerResult> results = new HashMap<Class, TransformerResult>();

    public TransformerResults(Transformer<?>[] transformers) {
        for (Transformer<?> transformer : transformers) {
            results.put(transformer.getClass(), TransformerResult.NEVER_RUN);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> TransformerResult<T> get(Class<? extends Transformer<T>> transformerClass) {
        TransformerResult<T> transformerResult = results.get(transformerClass);
        if (transformerResult == null) {
            throw new RuntimeException("Transformer not configured for this analysis: "
                    + transformerClass.getSimpleName());
        }
        return transformerResult;
    }

    public <T> void put(Class<? extends Transformer<T>> transformerClass, TransformerResult<T> transformerResult) {
        results.put(transformerClass, transformerResult);
    }

}
