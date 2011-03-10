package com.github.mread.turbulence4j.analysisapi;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisModulesSpikeTest {

    @Mock
    private Calculator<?> mockCalculator1;
    @Mock
    private Calculator<?> mockCalculator2;
    @Mock
    private Transformer<?> mockTransformer1;
    @Mock
    private Transformer<?> mockTransformer2;
    @Mock
    private Output mockOutput1;
    @Mock
    private Output mockOutput2;

    private Analysis mockedAnalysis;

    @Before
    public void setup() {
        mockedAnalysis = new FakeAnalysis(
                new Calculator[] { mockCalculator1, mockCalculator2 },
                new Transformer[] { mockTransformer1, mockTransformer2 },
                new Output[] { mockOutput1, mockOutput2 });
    }

    @Test
    public void anAnalysisCanBeClosedForReconfiguration() {
        new ClosedForReconfigurationAnalysis();
    }

    @Test
    public void itRunsEachCalculator() {
        mockedAnalysis.run();
        verify(mockCalculator1).run();
        verify(mockCalculator2).run();
    }

    @Test
    public void itRunsEachTransformer() {
        mockedAnalysis.run();
        verify(mockTransformer1).run(any(CalculatorResults.class));
        verify(mockTransformer2).run(any(CalculatorResults.class));
    }

    @Test
    public void itRunsEachOutput() {
        mockedAnalysis.run();
        verify(mockOutput1).run(any(TransformerResults.class));
        verify(mockOutput2).run(any(TransformerResults.class));
    }

    @Test
    public void transformersGetTheCalculatorResults() {
        Analysis analysis = new FakeAnalysis(
                new Calculator[] { new IntifierCalculator(5), new StringifierCalculator("abc") },
                new Transformer[] { mockTransformer1, mockTransformer2 },
                new Output[] { mockOutput1, mockOutput2 });

        analysis.run();
        verify(mockTransformer1).run(argThat(hasCalculatorResultsFor(IntifierCalculator.class)));
        verify(mockTransformer1).run(argThat(hasCalculatorResultsFor(StringifierCalculator.class)));
        verify(mockTransformer2).run(argThat(hasCalculatorResultsFor(IntifierCalculator.class)));
        verify(mockTransformer2).run(argThat(hasCalculatorResultsFor(StringifierCalculator.class)));
    }

    @Test
    public void outputsGetTheTransformerResults() {
        Analysis analysis = new FakeAnalysis(
                new Calculator[] { new IntifierCalculator(3), new StringifierCalculator("xyz") },
                new Transformer[] { new LongifierTransformer("xyz", 3, 0L) },
                new Output[] { mockOutput1, mockOutput2 });

        analysis.run();
        verify(mockOutput1).run(argThat(hasTransformerResultFor(LongifierTransformer.class)));
        verify(mockOutput2).run(argThat(hasTransformerResultFor(LongifierTransformer.class)));
    }

    @Test
    public void aTransformerThatWantsToGetStringsFromAStringCalculatorAndIntsFromAnIntCalculator() {

        Calculator<String> stringifier = new StringifierCalculator("abc");
        Calculator<Integer> intifier = new IntifierCalculator(35);
        Transformer<Long> transformer = new LongifierTransformer("abc", 35, 51);

        FakeAnalysis analysisOfStrings = new FakeAnalysis(
                new Calculator[] { stringifier, intifier },
                new Transformer[] { transformer },
                Output.NONE);

        analysisOfStrings.run();
    }

    private <T> Matcher<CalculatorResults> hasCalculatorResultsFor(
            final Class<? extends Calculator<T>> calculatorClass) {

        return new ArgumentMatcher<CalculatorResults>() {
            @Override
            public boolean matches(Object argument) {
                CalculatorResults calcResults = (CalculatorResults) argument;
                CalculatorResult<T> calculatorResult = calcResults.get(calculatorClass);
                return calculatorResult != CalculatorResult.NEVER_RUN;
            }
        };
    }

    private <T> Matcher<TransformerResults> hasTransformerResultFor(
            final Class<? extends Transformer<T>> transformerClass) {

        return new ArgumentMatcher<TransformerResults>() {
            @Override
            public boolean matches(Object argument) {
                TransformerResults transformerResults = (TransformerResults) argument;
                TransformerResult<T> transformerResult = transformerResults.get(transformerClass);
                return transformerResult != TransformerResult.NEVER_RUN;
            }
        };
    }
}
