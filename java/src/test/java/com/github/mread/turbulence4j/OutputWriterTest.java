package com.github.mread.turbulence4j;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.mread.calculators.FileValue;

public class OutputWriterTest {

    private static final File A_JAVA = new File("a", "a.java");
    private static final File B_JAVA = new File("b", "b.java");
    private OutputWriter outputWriter;
    private File destinationDirectory;

    @Before
    public void setup() throws IOException {
        destinationDirectory = new File("target/output-writer-test/");
        outputWriter = new OutputWriter(destinationDirectory);
    }

    @Test
    public void writesOutput() throws IOException {
        outputWriter.write(
                new File("."),
                asList(new FileValue(A_JAVA, 53),
                        new FileValue(B_JAVA, 25)),
                asList(new FileValue(A_JAVA, 3),
                        new FileValue(B_JAVA, 5)));

        assertThat(new File(destinationDirectory, OutputWriter.RAW_OUTPUT_TXT).exists(), equalTo(true));
        assertThat(new File(new File(destinationDirectory, "js/"), OutputWriter.DATASERIES_JS).exists(), equalTo(true));
    }

    @Test
    public void createsDatasetFromRawCalculatorInput() throws IOException {
        List<FileValue> churn = asList(
                new FileValue(A_JAVA, 53),
                new FileValue(B_JAVA, 25));
        List<FileValue> complexity = asList(
                new FileValue(A_JAVA, 3),
                new FileValue(B_JAVA, 5));

        Map<String, int[]> richData = outputWriter.transformData("", churn, complexity);

        assertThat(richData.get(A_JAVA.getCanonicalPath()),
                equalTo(new int[] { 53, 3 }));
        assertThat(richData.get(B_JAVA.getCanonicalPath()),
                equalTo(new int[] { 25, 5 }));
    }

    @Test
    public void matchingIndex() {
        assertThat(outputWriter.transformFilename("", "\\home\\a.java"), equalTo("\\home\\a.java"));
        assertThat(outputWriter.transformFilename("nonsense", "\\home\\a.java"), equalTo("\\home\\a.java"));
        assertThat(outputWriter.transformFilename("\\home", "\\home\\a.java"), equalTo("\\a.java"));
        assertThat(outputWriter.transformFilename("\\home\\", "\\home\\a.java"), equalTo("a.java"));
    }
}
