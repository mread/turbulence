package com.github.mread.turbulence4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.mread.calculators.FileValue;

public class OutputWriter {

    public static final String RAW_OUTPUT_TXT = "raw-output.txt";

    public void write(File prefixToTrim,
            File outputDirectory,
            List<FileValue> churn,
            List<FileValue> complexity) {

        outputDirectory.mkdirs();
        Map<String, int[]> richData = transformData(
                getCanonicalPath(prefixToTrim),
                churn,
                complexity);
        try {
            File rawOutput = new File(outputDirectory, RAW_OUTPUT_TXT);
            rawOutput.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(rawOutput);
            for (String file : richData.keySet()) {
                fileOutputStream.write((file + "\t").getBytes());
                fileOutputStream.write((richData.get(file)[0] + "\t").getBytes());
                fileOutputStream.write((richData.get(file)[1] + "\n").getBytes());
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Map<String, int[]> transformData(String prefixToTrim, List<FileValue> churn,
            List<FileValue> complexity) {

        Map<String, int[]> results = new TreeMap<String, int[]>();
        for (FileValue complexityFileValue : complexity) {
            for (FileValue churnFileValue : churn) {
                String complexityFilePath = complexityFileValue.getFilePath();
                String churnFilePath = churnFileValue.getFilePath();
                if (churnFilePath.equals(complexityFilePath)) {
                    String matchingFileNamePart = transformFilename(prefixToTrim, complexityFilePath);
                    int[] values = results.get(matchingFileNamePart);
                    if (values == null) {
                        results.put(
                                matchingFileNamePart,
                                new int[] { churnFileValue.getValue(), complexityFileValue.getValue() });
                    } else {
                        results.put(
                                matchingFileNamePart,
                                new int[] {
                                        values[0] + churnFileValue.getValue(),
                                        values[1] + complexityFileValue.getValue() });
                    }
                }
            }
        }
        return results;
    }

    String transformFilename(String prefixToTrim, String filePath) {
        if (prefixToTrim.isEmpty()) {
            return filePath.substring(0, filePath.lastIndexOf(File.separatorChar) + 1);
        }
        if (filePath.indexOf(prefixToTrim) != 0) {
            return filePath.substring(0, filePath.lastIndexOf(File.separatorChar) + 1);
        }
        return filePath.substring(prefixToTrim.length(), filePath.lastIndexOf(File.separatorChar) + 1);
    }

    private String getCanonicalPath(File prefixToTrim) {
        try {
            return prefixToTrim.getCanonicalPath() + File.separator;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
