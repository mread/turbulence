package com.github.mread.turbulence4j.analysisapi;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

class LongifierTransformer implements Transformer<Long> {
    private final String expectedString;
    private final int expectedInt;
    private final long result;

    public LongifierTransformer(String expectedString, int expectedInt, long result) {
        this.expectedString = expectedString;
        this.expectedInt = expectedInt;
        this.result = result;
    }

    @Override
    public TransformerResult<Long> run(CalculatorResults calculatorResults) {
        CalculatorResult<String> stringifierResult = calculatorResults.get(StringifierCalculator.class);
        String string = stringifierResult.getResult();
        assertThat(string, equalTo(expectedString));
        CalculatorResult<Integer> intifierResult = calculatorResults.get(IntifierCalculator.class);
        int integer = intifierResult.getResult();
        assertThat(integer, equalTo(expectedInt));
        return new TransformerResult<Long>() {
            @Override
            public Long getResult() {
                return result;
            }
        };
    }
}