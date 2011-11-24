package com.github.mread.turbulence4j.transformers;

import java.util.HashMap;
import java.util.Map;

import com.github.mread.turbulence4j.calculators.ComplexityAndNcss;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MergeMapsTransformerTest {

    private static final String A_JAVA = "a/a.java";
    private static final String B_JAVA = "b/b.java";

    private Map<String, Integer> map1;
    private Map<String, ComplexityAndNcss> map2;

    @Before
    public void setup() {

        map1 = new HashMap<String, Integer>();
        map1.put(A_JAVA, 53);
        map1.put(B_JAVA, 25);
        map2 = new HashMap<String, ComplexityAndNcss>();
        map2.put(A_JAVA, new ComplexityAndNcss(3, 15));
        map2.put(B_JAVA, new ComplexityAndNcss(5, 20));
    }

    @Test
    public void transformsRawCalculatorInputsIntoMap()  {

        MergeMapsTransformer transformer = new MergeMapsTransformer(null, null);
        transformer.transformData(map1, map2);
        Map<String, int[]> richData = transformer.getResults();

        assertThat(richData.get(A_JAVA), equalTo(new int[] { 53, 3 }));
        assertThat(richData.get(B_JAVA), equalTo(new int[] { 25, 5 }));
    }

}
