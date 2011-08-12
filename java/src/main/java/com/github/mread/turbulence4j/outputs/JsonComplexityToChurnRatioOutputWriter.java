package com.github.mread.turbulence4j.outputs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.calculators.AuthorValue;
import com.github.mread.turbulence4j.calculators.complexitycontribution.ComplexityContributionCalculator;

public class JsonComplexityToChurnRatioOutputWriter implements Output {

    static final String DATASERIES_JS = "c2cr-data.js";
    private final File destinationDirectory;
    private final ComplexityContributionCalculator complexityContributionCalculator;
    private String range;

    public JsonComplexityToChurnRatioOutputWriter(File destinationDirectory,
            ComplexityContributionCalculator complexityContributionCalculator) {
        this.destinationDirectory = destinationDirectory;
        this.complexityContributionCalculator = complexityContributionCalculator;
    }

    @Override
    public void output() {
        try {
            write(complexityContributionCalculator.getResults());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(List<AuthorValue> data) throws IOException {
        File jsonOutput = new File(destinationDirectory, DATASERIES_JS);
        destinationDirectory.mkdirs();
        jsonOutput.createNewFile();

        try {
            JSONArray categories = new JSONArray();
            JSONArray values = new JSONArray();
            for (AuthorValue av : data) {
                categories.put(av.getAuthor());
                values.put(av.getValue());
            }

            FileWriter writer = new FileWriter(jsonOutput);
            writeVar(writer, "categories", categories);
            writeVar(writer, "values", values);
            writer.append("var range = '" + range + "';\n");
            writer.close();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeVar(FileWriter writer, String var, JSONArray categories) throws IOException, JSONException {
        writer.append("var " + var + " = ");
        categories.write(writer);
        writer.append(";\n");
    }

    @Override
    public void setRange(String range) {
        this.range = range;
    }

}
