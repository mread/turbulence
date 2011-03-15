package com.github.mread.turbulence4j.outputs;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.calculators.AuthorFileValue;
import com.github.mread.turbulence4j.calculators.AuthorFilenameKey;
import com.github.mread.turbulence4j.calculators.AuthorValue;
import com.github.mread.turbulence4j.calculators.ComplexityContributionCalculator;

public class JsonComplexityByAuthorOutputWriter implements Output {

    static final String DATASERIES_JS = "c2a-data.js";
    private final File destinationDirectory;
    private final ComplexityContributionCalculator complexityContributionCalculator;

    public JsonComplexityByAuthorOutputWriter(File destinationDirectory,
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

    private List<AuthorFileValue> sortedByChurn(Map<String, Integer> authorTotalChurn) {
        List<AuthorFileValue> sortableEntries = new ArrayList<AuthorFileValue>();
        for (String author : authorTotalChurn.keySet()) {
            AuthorFileValue entry = new AuthorFileValue(author, authorTotalChurn.get(author), null);
            sortableEntries.add(entry);
        }
        return sort(sortableEntries, on(AuthorFileValue.class).getChurn());
    }

    private Map<String, Integer> churnByAuthor(Map<AuthorFilenameKey, Integer> richData) {
        Map<String, Integer> authorTotalChurn = new HashMap<String, Integer>();
        List<String> authors = extract(richData.keySet(), on(AuthorFilenameKey.class).getAuthor());
        for (String author : authors) {
            authorTotalChurn.put(author, 0);
        }
        for (AuthorFilenameKey authorFilename : richData.keySet()) {
            Integer currentTotalChurn = authorTotalChurn.get(authorFilename.getAuthor());
            Integer churnValue = richData.get(authorFilename);
            authorTotalChurn.put(authorFilename.getAuthor(), currentTotalChurn + churnValue);
        }
        return authorTotalChurn;
    }
}
