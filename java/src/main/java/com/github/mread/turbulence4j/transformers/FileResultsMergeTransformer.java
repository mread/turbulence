package com.github.mread.turbulence4j.transformers;

import java.util.Map;
import java.util.TreeMap;

import com.github.mread.turbulence4j.analysisapi.CalculatorResults;
import com.github.mread.turbulence4j.analysisapi.Transformer;
import com.github.mread.turbulence4j.analysisapi.TransformerResult;
import com.github.mread.turbulence4j.calculators.ChurnCalculator;
import com.github.mread.turbulence4j.calculators.ComplexityCalculator;

public class FileResultsMergeTransformer implements Transformer<Map<String, int[]>> {

    @Override
    public FileResultsMergeTransformerResult run(CalculatorResults calculatorResults) {
        Map<String, Integer> churn = calculatorResults.get(ChurnCalculator.class).getResult();
        Map<String, Integer> complexity = calculatorResults.get(ComplexityCalculator.class).getResult();
        return new FileResultsMergeTransformerResult(transformData(churn, complexity));
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

    public static class FileResultsMergeTransformerResult implements TransformerResult<Map<String, int[]>> {

        private final Map<String, int[]> result;

        public FileResultsMergeTransformerResult(Map<String, int[]> result) {
            this.result = result;
        }

        @Override
        public Map<String, int[]> getResult() {
            return result;
        }

    }

}
