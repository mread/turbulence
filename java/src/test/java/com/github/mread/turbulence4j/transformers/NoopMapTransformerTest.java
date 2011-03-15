package com.github.mread.turbulence4j.transformers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.mread.turbulence4j.calculators.AuthorFilenameKey;
import com.github.mread.turbulence4j.calculators.ChurnByAuthorCalculator;

public class NoopMapTransformerTest {

    private static final Map<AuthorFilenameKey, Integer> DATA = new HashMap<AuthorFilenameKey, Integer>();

    @Test
    public void returnsTheSameMapAsItWasGiven() {

        ChurnByAuthorCalculator churnCalculator = mock(ChurnByAuthorCalculator.class);
        when(churnCalculator.getResults()).thenReturn(DATA);

        NoopMapTransformer<AuthorFilenameKey, Integer> noopMapTransformer =
                new NoopMapTransformer<AuthorFilenameKey, Integer>(churnCalculator);
        noopMapTransformer.transform();

        assertThat(noopMapTransformer.getResults(), equalTo(DATA));
    }
}
