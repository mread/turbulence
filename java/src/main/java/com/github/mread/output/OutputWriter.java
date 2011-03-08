package com.github.mread.output;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class OutputWriter {

    private final CanWriteOutput[] writers;

    public OutputWriter(CanWriteOutput[] writers) {
        this.writers = writers;
    }

    public void write(File prefixToTrim,
            Map<String, Integer> churn,
            Map<String, Integer> complexity) {

        Map<String, int[]> richData = transformData(getCanonicalPath(prefixToTrim), churn, complexity);
        try {
            for (CanWriteOutput writer : writers) {
                writer.write(richData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    Map<String, int[]> transformData(
            String prefixToTrim,
            Map<String, Integer> churn,
            Map<String, Integer> complexity) {

        Map<String, int[]> results = new TreeMap<String, int[]>();
        for (String complexityEntryFileName : complexity.keySet()) {
            if (!churn.containsKey(complexityEntryFileName)) {
                // no churn at all - not even zero - probably not in git yet
                continue;
            }
            String interestingFilenameFragment = transformFilename(prefixToTrim, complexityEntryFileName);
            int churnValue = churn.get(complexityEntryFileName);
            int complexityValue = complexity.get(complexityEntryFileName);
            int[] existingValues = results.get(interestingFilenameFragment);
            if (existingValues == null) {
                existingValues = new int[] { 0, 0 };
            }
            results.put(interestingFilenameFragment, new int[] {
                    existingValues[0] + churnValue,
                    existingValues[1] + complexityValue
            });
        }
        return results;
    }

    String transformFilename(String prefixToTrim, String filePath) {
        if (prefixToTrim.isEmpty() || filePath.indexOf(prefixToTrim) != 0) {
            return filePath;
        }
        return filePath.substring(prefixToTrim.length());
    }

    private String getCanonicalPath(File prefixToTrim) {
        try {
            return prefixToTrim.getCanonicalPath() + File.separator;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
