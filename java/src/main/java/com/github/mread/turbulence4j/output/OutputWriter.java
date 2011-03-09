package com.github.mread.turbulence4j.output;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.github.mread.turbulence4j.analysisapi.CalculatorResults;
import com.github.mread.turbulence4j.analysisapi.Transformer;
import com.github.mread.turbulence4j.analysisapi.TransformerResult;

public class OutputWriter implements Transformer {

    private final CanWriteOutput[] writers;

    public OutputWriter(CanWriteOutput[] writers) {
        this.writers = writers;
    }

    @Override
    public TransformerResult run(CalculatorResults calculatorResults) {
        return null;
    }

    public void write(Map<String, Integer> churn, Map<String, Integer> complexity) {

        Map<String, int[]> richData = transformData(churn, complexity);
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

}
