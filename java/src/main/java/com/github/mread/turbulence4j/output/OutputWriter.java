package com.github.mread.turbulence4j.output;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.github.mread.turbulence4j.analysisapi.CalculatorResults;
import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.analysisapi.Transformer;
import com.github.mread.turbulence4j.analysisapi.TransformerResult;
import com.github.mread.turbulence4j.analysisapi.TransformerResults;
import com.github.mread.turbulence4j.calculators.ChurnCalculator;
import com.github.mread.turbulence4j.calculators.ComplexityCalculator;

public class OutputWriter implements Transformer<Map<String, int[]>>, Output {

    private final CanWriteOutput[] writers;

    public OutputWriter(CanWriteOutput[] writers) {
        this.writers = writers;
    }

    @Override
    public OutputWriterTransformerResult run(CalculatorResults calculatorResults) {
        Map<String, Integer> churn = calculatorResults.get(ChurnCalculator.class).getResult();
        Map<String, Integer> complexity = calculatorResults.get(ComplexityCalculator.class).getResult();
        return new OutputWriterTransformerResult(transformData(churn, complexity));
    }

    @Override
    public void run(TransformerResults transformerResults) {
        Map<String, int[]> result = transformerResults.get(OutputWriter.class).getResult();
        doWrites(result);
    }

    public void write(Map<String, Integer> churn, Map<String, Integer> complexity) {

        Map<String, int[]> richData = transformData(churn, complexity);
        doWrites(richData);

    }

    private void doWrites(Map<String, int[]> richData) {
        try {
            for (CanWriteOutput writer : writers) {
                writer.write(richData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Map<String, int[]> transformData(Map<String, Integer> churn, Map<String, Integer> complexity) {

        Map<String, int[]> results = new TreeMap<String, int[]>();
        for (String complexityEntryFileName : complexity.keySet()) {
            if (!churn.containsKey(complexityEntryFileName)) {
                // no churn at all - not even zero - probably not in git yet
                continue;
            }
            int churnValue = churn.get(complexityEntryFileName);
            int complexityValue = complexity.get(complexityEntryFileName);
            int[] existingValues = results.get(complexityEntryFileName);
            if (existingValues == null) {
                existingValues = new int[] { 0, 0 };
            }
            results.put(complexityEntryFileName, new int[] {
                    existingValues[0] + churnValue,
                    existingValues[1] + complexityValue
            });
        }
        return results;
    }

    public class OutputWriterTransformerResult implements TransformerResult<Map<String, int[]>> {

        private final Map<String, int[]> result;

        public OutputWriterTransformerResult(Map<String, int[]> result) {
            this.result = result;
        }

        @Override
        public Map<String, int[]> getResult() {
            return result;
        }

    }

}
