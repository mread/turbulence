package com.github.mread.turbulence4j.transformers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class FileResultsMergeTransformerTest {

    private static final String A_JAVA = "a/a.java";
    private static final String B_JAVA = "b/b.java";

    private Map<String, Integer> exampleChurn;
    private Map<String, Integer> exampleComplexity;

    @Before
    public void setup() throws IOException {

        exampleChurn = new HashMap<String, Integer>();
        exampleChurn.put(A_JAVA, 53);
        exampleChurn.put(B_JAVA, 25);
        exampleComplexity = new HashMap<String, Integer>();
        exampleComplexity.put(A_JAVA, 3);
        exampleComplexity.put(B_JAVA, 5);
    }

    @Test
    public void transformsRawCalculatorInputIntoMap() throws IOException {

        Map<String, int[]> richData = new FileResultsMergeTransformer().transformData(exampleChurn, exampleComplexity);

        assertThat(richData.get(A_JAVA), equalTo(new int[] { 53, 3 }));
        assertThat(richData.get(B_JAVA), equalTo(new int[] { 25, 5 }));
    }

}
