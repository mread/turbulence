package com.github.mread.turbulence4j.analysisapi3;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class BaseTransformerTest {

    @Test
    public void providesResultToTheOutput() {

        BaseTransformer transformer = aTransformerWithResult(new Integer(12));
        Output output = anOutput();
        transformer.setOutputs(new Output[] { output });

        transformer.run();

        verify(output).setResultOfTransformer(eq(transformer), eq(12));

    }

    private static Output anOutput() {
        return mock(Output.class);
    }

    private static BaseTransformer aTransformerWithResult(final Object result) {
        BaseTransformer transformer = mock(BaseTransformer.class);
        when(transformer.transform()).thenReturn(result);
        return transformer;
    }

}
