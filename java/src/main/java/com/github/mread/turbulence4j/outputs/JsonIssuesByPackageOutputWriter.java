package com.github.mread.turbulence4j.outputs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.calculators.FileValue;
import com.github.mread.turbulence4j.transformers.CountPackageMentionsTransformer;

public class JsonIssuesByPackageOutputWriter implements Output {

    static final String DATASERIES_JS = "ibp-data.js";
    private final File destinationDirectory;
    private final CountPackageMentionsTransformer countPackageMentionsTransformer;
    private String range = "";

    public JsonIssuesByPackageOutputWriter(File destinationDirectory,
            CountPackageMentionsTransformer countClassMentionsTransformer) {

        this.destinationDirectory = destinationDirectory;
        this.countPackageMentionsTransformer = countClassMentionsTransformer;
    }

    @Override
    public void output() {
        try {
            write(countPackageMentionsTransformer.getResults());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(List<FileValue> results) throws IOException {
        File jsonOutput = new File(destinationDirectory, DATASERIES_JS);
        destinationDirectory.mkdirs();
        jsonOutput.createNewFile();

        try {
            JSONArray categories = new JSONArray();
            JSONArray values = new JSONArray();
            for (FileValue entry : results) {
                categories.put(entry.getFilename());
                values.put(entry.getValue());
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

    @Override
    public void setRange(String range) {
        this.range = range;
    }

    private void writeVar(FileWriter writer, String var, JSONArray categories) throws IOException, JSONException {
        writer.append("var " + var + " = ");
        categories.write(writer);
        writer.append(";\n");
    }

}
