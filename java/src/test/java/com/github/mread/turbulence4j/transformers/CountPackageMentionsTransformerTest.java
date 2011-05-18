package com.github.mread.turbulence4j.transformers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.turbulence4j.calculators.DistinctIssueCommitsPerPackageCalculator;
import com.github.mread.turbulence4j.calculators.IssueTypeCalculator;

@RunWith(MockitoJUnitRunner.class)
public class CountPackageMentionsTransformerTest {

    @Mock
    private DistinctIssueCommitsPerPackageCalculator mockCalculator1;
    @Mock
    private IssueTypeCalculator mockCalculator2;
    private CountPackageMentionsTransformer transformer;

    @Before
    public void setup() {
        transformer = new CountPackageMentionsTransformer(
                mockCalculator1,
                mockCalculator2);
    }

    @Test
    public void appliesDepthToPackageNames() {

        assertThat(transformer.rollupPacknameToDepth("a.b.c.d", 2), equalTo("a.b"));
        assertThat(transformer.rollupPacknameToDepth("a.b.c.d", 3), equalTo("a.b.c"));
        assertThat(transformer.rollupPacknameToDepth("a.b", 4), equalTo("a.b"));
        assertThat(transformer.rollupPacknameToDepth("", 4), equalTo(""));
        assertThat(transformer.rollupPacknameToDepth("a.b", 0), equalTo(""));
    }

    @Test
    public void appliesPackageNameTransformations() {

        Map<String, String> transformations = new HashMap<String, String>();
        transformations.put("a.b.c", "x.y");
        transformations.put("a1.b1.c1", "new.pack");
        transformations.put("a1.b2.c1", "new.pack");

        transformer.setTransformations(transformations);

        assertThat(transformer.transformPackageName("a.b.c"), equalTo("x.y"));
        assertThat(transformer.transformPackageName("a.b.c.d"), equalTo("x.y.d"));
        assertThat(transformer.transformPackageName("a.xyz"), equalTo("a.xyz"));
        assertThat(transformer.transformPackageName("a1.b1.c1.x.y.z"), equalTo("new.pack.x.y.z"));
        assertThat(transformer.transformPackageName("a1.b2.c1.x.y.z"), equalTo("new.pack.x.y.z"));
    }
}
