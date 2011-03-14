package com.github.mread.turbulence4j.transformers;

import java.util.Map;

import com.github.mread.turbulence4j.analysisapi.Transformer;
import com.github.mread.turbulence4j.calculators.AuthorFilenameKey;
import com.github.mread.turbulence4j.calculators.ChurnByAuthorCalculator;

public class NoopMapTransformer implements Transformer<Map<AuthorFilenameKey, Integer>> {

    private final ChurnByAuthorCalculator churnCalculator;
    private Map<AuthorFilenameKey, Integer> results;

    public NoopMapTransformer(ChurnByAuthorCalculator churnCalculator) {
        this.churnCalculator = churnCalculator;
    }

    @Override
    public void transform() {
        this.results = churnCalculator.getResults();
    }

    @Override
    public Map<AuthorFilenameKey, Integer> getResults() {
        return results;
    }
}
