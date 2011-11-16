package com.github.mread.turbulence4j.transformers;

import java.util.Map;
import java.util.TreeMap;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.Transformer;

public class MergeMapsTransformer implements Transformer<Map<String, int[]>> {

    private final Calculator<Map<String, Integer>> churnCalculator;
    private final Calculator<Map<String, Integer>> complexityCalculator;

    private Map<String, int[]> results = new TreeMap<String, int[]>();

    public MergeMapsTransformer(Calculator<Map<String, Integer>> churnCalculator,
            Calculator<Map<String, Integer>> complexityCalculator) {

        this.churnCalculator = churnCalculator;
        this.complexityCalculator = complexityCalculator;
    }

    @Override
    public void transform() {
        transformData(churnCalculator.getResults(), complexityCalculator.getResults());
    }

    @Override
    public Map<String, int[]> getResults() {
        return results;
    }

    void transformData(Map<String, Integer> churn, Map<String, Integer> complexity) {

        for (String complexityEntryFileName : complexity.keySet()) {
            if (!churn.containsKey(complexityEntryFileName)) {
                // no churn at all - not even zero - probably not in git yet
//                System.err.println("No churn result for: " + complexityEntryFileName);
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
    }

}
