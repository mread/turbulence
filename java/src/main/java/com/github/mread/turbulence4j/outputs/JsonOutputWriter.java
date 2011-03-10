package com.github.mread.turbulence4j.outputs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.analysisapi.TransformerResults;
import com.github.mread.turbulence4j.transformers.FileResultsMergeTransformer;

public class JsonOutputWriter implements Output {

    static final String DATASERIES_JS = "data.js";
    private final File destinationDirectory;

    public JsonOutputWriter(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    public void run(TransformerResults transformerResults) {
        Map<String, int[]> result = transformerResults.get(FileResultsMergeTransformer.class).getResult();
        try {
            write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(Map<String, int[]> richData) throws IOException {
        File jsonOutput = new File(destinationDirectory, DATASERIES_JS);
        destinationDirectory.mkdirs();
        jsonOutput.createNewFile();

        JSONObject root = new JSONObject();
        JSONArray src = new JSONArray();
        JSONArray test = new JSONArray();
        JSONArray other = new JSONArray();

        try {
            for (String file : richData.keySet()) {
                JSONObject row = new JSONObject();
                row.put("filename", file);
                row.put("x", richData.get(file)[0]);
                row.put("y", richData.get(file)[1]);
                if (file.contains("src/main")) {
                    src.put(row);
                } else if (file.contains("src/test")) {
                    test.put(row);
                } else {
                    other.put(row);
                }
            }
            // seems to render JSON in reverse order of puts
            // so put them in reverse of how we want to see them
            if (other.length() > 0) {
                root.put("other", other);
            }
            root.put("src/test/java", test);
            root.put("src/main/java", src);

            FileWriter writer = new FileWriter(jsonOutput);
            writer.append("var directorySeries = ");
            root.write(writer);
            writer.close();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
