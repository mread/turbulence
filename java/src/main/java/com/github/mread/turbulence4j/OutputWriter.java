package com.github.mread.turbulence4j;

import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.mread.calculators.FileValue;
import com.google.gwt.dev.json.JsonArray;
import com.google.gwt.dev.json.JsonObject;

public class OutputWriter {

    static final String RAW_OUTPUT_TXT = "raw-output.txt";
    static final String DATASERIES_JS = "data.js";

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
            writeRaw(outputDirectory, richData);
            writeJson(outputDirectory, richData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeJson(File outputDirectory, Map<String, int[]> richData) throws IOException {
        File jsonOutput = new File(outputDirectory, DATASERIES_JS);

        JsonObject json = JsonObject.create();
        JsonArray src = JsonArray.create();
        JsonArray test = JsonArray.create();
        JsonArray other = JsonArray.create();

        for (String file : richData.keySet()) {
            JsonObject row = JsonObject.create();
            row.put("filename", file);
            row.put("x", richData.get(file)[0]);
            row.put("y", richData.get(file)[1]);
            if (file.startsWith("src\\main")) {
                src.add(row);
            } else if (file.startsWith("src\\test")) {
                test.add(row);
            } else {
                other.add(row);
            }
        }
        json.put("src/main/java", src);
        json.put("src/test/java", test);
        if (other.getLength() > 0) {
            json.put("other", other);
        }

        FileWriter writer = new FileWriter(jsonOutput);
        writer.append("var directorySeries = ");
        json.write(writer);
        writer.close();
    }

    private void writeRaw(File outputDirectory, Map<String, int[]> richData) throws IOException, FileNotFoundException {
        File rawOutput = new File(outputDirectory, RAW_OUTPUT_TXT);
        rawOutput.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(rawOutput);
        for (String file : richData.keySet()) {
            fileOutputStream.write((file + "\t").getBytes());
            fileOutputStream.write((richData.get(file)[0] + "\t").getBytes());
            fileOutputStream.write((richData.get(file)[1] + "\n").getBytes());
        }
        fileOutputStream.close();
    }

    Map<String, int[]> transformData(String prefixToTrim, List<FileValue> churn,
            List<FileValue> complexity) {

        Map<String, FileValue> indexedChurn = index(churn, on(FileValue.class).getFilePath());

        Map<String, int[]> results = new TreeMap<String, int[]>();
        for (FileValue complexityFileValue : complexity) {
            String complexityFilePath = complexityFileValue.getFilePath();
            FileValue churnFileValue = indexedChurn.get(complexityFilePath);
            if (churnFileValue == null) {
                // no churn at all - not even zero
                continue;
            }

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
        return results;
    }

    String transformFilename(String prefixToTrim, String filePath) {
        if (prefixToTrim.isEmpty()) {
            return filePath;
        }
        if (filePath.indexOf(prefixToTrim) != 0) {
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
