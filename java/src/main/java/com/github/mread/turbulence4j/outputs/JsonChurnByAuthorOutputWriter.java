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
import com.github.mread.turbulence4j.transformers.NoopMapTransformer;

public class JsonChurnByAuthorOutputWriter implements Output {

    static final String DATASERIES_JS = "ca-data.js";
    private final File destinationDirectory;
    private final NoopMapTransformer<AuthorFilenameKey, Integer> transformer;

    public JsonChurnByAuthorOutputWriter(File destinationDirectory,
            NoopMapTransformer<AuthorFilenameKey, Integer> transformer) {
        this.destinationDirectory = destinationDirectory;
        this.transformer = transformer;
    }

    @Override
    public void output() {
        try {
            write(transformer.getResults());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(Map<AuthorFilenameKey, Integer> richData) throws IOException {
        File jsonOutput = new File(destinationDirectory, DATASERIES_JS);
        destinationDirectory.mkdirs();
        jsonOutput.createNewFile();

        try {
            List<AuthorFileValue> sorted = sortedByChurn(churnByAuthor(richData));

            JSONArray categories = new JSONArray();
            JSONArray values = new JSONArray();
            for (AuthorFileValue afv : sorted) {
                categories.put(afv.getAuthor());
                values.put(afv.getChurn());
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
