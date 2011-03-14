package com.github.mread.turbulence4j.outputs;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class JsonOutputWriterTest {

    private static final String A_JAVA = "a/a.java";
    private static final String B_JAVA = "b/b.java";

    private Map<String, int[]> exampleData = new HashMap<String, int[]>();
    private File destinationDirectory;

    @Before
    public void setup() {
        exampleData.put(A_JAVA, new int[] { 3, 5 });
        exampleData.put(B_JAVA, new int[] { 30, 53 });
        destinationDirectory = new File("target/json-output-writer-test/");
        destinationDirectory.delete();
    }

    @Test
    public void writesOutput() throws IOException {
        JsonOutputWriter outputWriter = new JsonOutputWriter(destinationDirectory, null);
        outputWriter.write(exampleData);
        assertThat(new File(destinationDirectory, JsonOutputWriter.DATASERIES_JS).exists(), equalTo(true));
    }

}
