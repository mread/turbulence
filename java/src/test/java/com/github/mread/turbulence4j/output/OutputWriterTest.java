package com.github.mread.turbulence4j.output;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.mread.turbulence4j.output.CanWriteOutput;
import com.github.mread.turbulence4j.output.JsonOutputWriter;
import com.github.mread.turbulence4j.output.OutputWriter;
import com.github.mread.turbulence4j.output.RawOutputWriter;

public class OutputWriterTest {

    private static final String A_JAVA = "a/a.java";
    private static final String B_JAVA = "b/b.java";

    private OutputWriter outputWriter;
    private File destinationDirectory;
    private Map<String, Integer> exampleChurn;
    private Map<String, Integer> exampleComplexity;

    @Before
    public void setup() throws IOException {
        destinationDirectory = new File("target/output-writer-test/");
        destinationDirectory.delete();
        CanWriteOutput[] writers = new CanWriteOutput[] {
                new RawOutputWriter(destinationDirectory),
                new JsonOutputWriter(new File(destinationDirectory, "js")) };

        outputWriter = new OutputWriter(writers);

        exampleChurn = new HashMap<String, Integer>();
        exampleChurn.put(A_JAVA, 53);
        exampleChurn.put(B_JAVA, 25);
        exampleComplexity = new HashMap<String, Integer>();
        exampleComplexity.put(A_JAVA, 3);
        exampleComplexity.put(B_JAVA, 5);
    }

    @Test
    public void writesOutput() throws IOException {

        outputWriter.write(exampleChurn, exampleComplexity);

        assertThat(new File(destinationDirectory, RawOutputWriter.RAW_OUTPUT_TXT).exists(),
                equalTo(true));
        assertThat(new File(new File(destinationDirectory, "js/"), JsonOutputWriter.DATASERIES_JS).exists(),
                equalTo(true));
    }

    @Test
    public void transformsRawCalculatorInputIntoMap() throws IOException {

        Map<String, int[]> richData = outputWriter.transformData(exampleChurn, exampleComplexity);

        assertThat(richData.get(A_JAVA), equalTo(new int[] { 53, 3 }));
        assertThat(richData.get(B_JAVA), equalTo(new int[] { 25, 5 }));
    }

}
