package com.github.mread.turbulence4j.transformers;

import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.Transformer;

public class NoopMapTransformer<K, V> implements Transformer<Map<K, V>> {

    private final Calculator<Map<K, V>> mapCalculator;
    private Map<K, V> results;

    public NoopMapTransformer(Calculator<Map<K, V>> mapCalculator) {
        this.mapCalculator = mapCalculator;
    }

    @Override
    public void transform() {
        this.results = mapCalculator.getResults();
    }

    @Override
    public Map<K, V> getResults() {
        return results;
    }
}
