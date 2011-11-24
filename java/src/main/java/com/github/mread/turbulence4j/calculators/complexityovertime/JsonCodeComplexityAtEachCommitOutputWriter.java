package com.github.mread.turbulence4j.calculators.complexityovertime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.mread.turbulence4j.analysisapi.Output;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonCodeComplexityAtEachCommitOutputWriter implements Output {

    static final String DATASERIES_JS = "cot-data.js";

    private String range;
    private final File outputDirectory;
    private final CodeComplexityAtEachCommitCalculator calculator;

    public JsonCodeComplexityAtEachCommitOutputWriter(File outputDirectory,
            CodeComplexityAtEachCommitCalculator codeComplexityAtEachCommitCalculator) {
        this.outputDirectory = outputDirectory;
        this.calculator = codeComplexityAtEachCommitCalculator;
    }

    @Override
    public void output() {
        try {
            File jsonOutput = new File(outputDirectory, DATASERIES_JS);
            outputDirectory.mkdirs();
            jsonOutput.createNewFile();

            JSONArray complexities = new JSONArray();
            JSONArray ncss = new JSONArray();

            for (CommitTimeWithComplexityAndNcss entry : calculator.getResults()) {
                JSONArray complexityEntry = new JSONArray();
                complexityEntry.put(entry.getTimestamp() * 1000);
                complexityEntry.put(entry.getComplexity());
                complexities.put(complexityEntry);

                JSONArray ncssEntry = new JSONArray();
                ncssEntry.put(entry.getTimestamp() * 1000);
                ncssEntry.put(entry.getNcss());
                ncss.put(ncssEntry);
            }

            FileWriter writer = new FileWriter(jsonOutput);
            writeVar(writer, "complexities", complexities);
            writeVar(writer, "ncss", ncss);
            writer.append("var range = '" + range + "';\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeVar(FileWriter writer, String var, JSONArray jsonData) throws IOException, JSONException {
        writer.append("var " + var + " = ");
        jsonData.write(writer);
        writer.append(";\n");
    }

    @Override
    public void setRange(String range) {
        this.range = range;

    }

}
