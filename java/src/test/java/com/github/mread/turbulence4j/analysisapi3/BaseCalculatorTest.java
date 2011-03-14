package com.github.mread.turbulence4j.analysisapi3;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class BaseCalculatorTest {

    @Test
    public void providesResultToTheTransformer() {

        BaseCalculator calculator = aCalculatorWithResult(new Integer(35));
        Transformer transformer = aTransformer();
        calculator.setTransformers(new Transformer[] { transformer });

        calculator.run();

        verify(transformer).setResultOfCalculator(eq(calculator), eq(35));

    }

    private static Transformer aTransformer() {
        return mock(Transformer.class);
    }

    private static BaseCalculator aCalculatorWithResult(final Object result) {
        BaseCalculator calculator = mock(BaseCalculator.class);
        when(calculator.calculate()).thenReturn(result);
        return calculator;
    }
}
