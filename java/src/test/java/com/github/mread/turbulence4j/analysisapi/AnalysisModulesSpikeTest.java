package com.github.mread.turbulence4j.analysisapi;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.turbulence4j.analysis.closed.ClosedForReconfigurationAnalysis;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisModulesSpikeTest {

    @Mock
    private Calculator mockCalculator1;
    @Mock
    private Calculator mockCalculator2;
    @Mock
    private Transformer mockTransformer1;
    @Mock
    private Transformer mockTransformer2;
    @Mock
    private Output mockOutput1;
    @Mock
    private Output mockOutput2;

    private Analysis mockedAnalysis;

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
        verify(mockOutput1).run(anyTransformerResults());
        verify(mockOutput2).run(anyTransformerResults());
    }

    @Test
    public void transformersGetTheCalculatorResults() {
        mockedAnalysis.run();
        verify(mockTransformer1).run(argThat(hasCalculatorResultsFor(mockCalculator1)));
        verify(mockTransformer1).run(argThat(hasCalculatorResultsFor(mockCalculator2)));
        verify(mockTransformer2).run(argThat(hasCalculatorResultsFor(mockCalculator1)));
        verify(mockTransformer2).run(argThat(hasCalculatorResultsFor(mockCalculator2)));
    }

    @Test
    public void outputsGetTheTransformerResults() {
        mockedAnalysis.run();
        verify(mockOutput1).run(argThat(hasTransformerResultFor(mockTransformer1)));
        verify(mockOutput1).run(argThat(hasTransformerResultFor(mockTransformer2)));
        verify(mockOutput2).run(argThat(hasTransformerResultFor(mockTransformer1)));
        verify(mockOutput2).run(argThat(hasTransformerResultFor(mockTransformer2)));
    }

    @Test
    public void aCalculatorThatProducesAStringCanBeTransformedByAStringParsingTransformer() {

        final Calculator stringOutputtingCalculator = new Calculator() {
            @Override
            public CalculatorResult run() {
                return new CalculatorResult() {
                    @Override
                    public Object getResult() {
                        return "abc";
                    }
                };
            }
        };
        Transformer stringParsingTransformer = new Transformer() {
            @Override
            public TransformerResult run(CalculatorResults calculatorResults) {
                CalculatorResult calculatorResult = calculatorResults.get(stringOutputtingCalculator);
                String result = (String) calculatorResult.getResult();
                assertThat(result, equalTo("abc"));
                return null;
            }
        };

        FakeAnalysis analysisOfStrings = new FakeAnalysis(new Calculator[] { stringOutputtingCalculator },
                new Transformer[] { stringParsingTransformer },
                Output.NONE);

        analysisOfStrings.run();
    }

    @Before
    public void setup() {
        mockedAnalysis = new FakeAnalysis(
                new Calculator[] { mockCalculator1, mockCalculator2 },
                new Transformer[] { mockTransformer1, mockTransformer2 },
                new Output[] { mockOutput1, mockOutput2 });
    }

    private Matcher<CalculatorResults> hasCalculatorResultsFor(final Calculator calculator) {
        return new ArgumentMatcher<CalculatorResults>() {
            @Override
            public boolean matches(Object argument) {
                CalculatorResults arg = (CalculatorResults) argument;
                return arg.get(calculator) != CalculatorResult.NEVER_RUN;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Map<Transformer, TransformerResult> anyTransformerResults() {
        return any(Map.class);
    }

    private Matcher<Map<Transformer, TransformerResult>> hasTransformerResultFor(Transformer transformer) {
        return hasKey(transformer);
    }
}
