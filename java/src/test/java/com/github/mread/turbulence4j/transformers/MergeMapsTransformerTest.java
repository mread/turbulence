package com.github.mread.turbulence4j.transformers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MergeMapsTransformerTest {

    private static final String A_JAVA = "a/a.java";
    private static final String B_JAVA = "b/b.java";

    private Map<String, Integer> map1;
    private Map<String, Integer> map2;

    @Before
    public void setup() throws IOException {

        map1 = new HashMap<String, Integer>();
        map1.put(A_JAVA, 53);
        map1.put(B_JAVA, 25);
        map2 = new HashMap<String, Integer>();
        map2.put(A_JAVA, 3);
        map2.put(B_JAVA, 5);
    }

    @Test
    public void transformsRawCalculatorInputIntoMap() throws IOException {

        Map<String, int[]> richData = new MergeMapsTransformer().transformData(map1, map2);

        assertThat(richData.get(A_JAVA), equalTo(new int[] { 53, 3 }));
        assertThat(richData.get(B_JAVA), equalTo(new int[] { 25, 5 }));
    }

}
