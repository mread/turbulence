package com.github.mread.turbulence4j.calculators;

import static com.github.mread.turbulence4j.calculators.ChurnByAuthorCalculator.keyOf;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ChurnByAuthorCalculatorTest {

    private static final File WORKING_DIRECTORY = new File("src/test/java/com/github/mread/turbulence4j/calculators/");

    @Mock
    private JavaFileFinder mockFileFinder;
    @Mock
    private GitAdapter mockGitAdapter;

    private ChurnByAuthorCalculator calculator;

    @Before
    public void setup() {
        calculator = new ChurnByAuthorCalculator(
                WORKING_DIRECTORY,
                mockFileFinder,
                mockGitAdapter);
    }

    @Test
    public void noFilesMeansNoScores() {
        when(mockFileFinder.findAllJavaFiles()).thenReturn(Collections.<String> emptyList());
        when(mockGitAdapter.getLogWithAuthor(WORKING_DIRECTORY)).thenReturn(Collections.<String> emptyList());
        calculator.calculate();
        Map<AuthorFilenameKey, Integer> result = calculator.getResults();
        assertThat(result.size(), equalTo(0));
    }

    @Test
    public void returnsFileAuthorPairsWithAChurnScore() {

        when(mockFileFinder.findAllJavaFiles()).thenReturn(asList(
                "a/1.java",
                "b/2.java"
                ));
        when(mockGitAdapter.getLogWithAuthor(WORKING_DIRECTORY)).thenReturn(asList(
                "joe\t1\t1\ta/1.java",
                "fred\t2\t2\ta/1.java"
                ));

        calculator.calculate();
        Map<AuthorFilenameKey, Integer> result = calculator.getResults();

        assertThat(result.get(keyOf("joe", "a/1.java")), equalTo(2));
        assertThat(result.get(keyOf("fred", "a/1.java")), equalTo(4));
    }

    @Test
    public void canStructureLogLines() {

        List<String> logLines = asList(
                "joe\t1\t1\ta/1.java",
                "fred\t2\t2\ta1.java");

        List<String[]> entries = calculator.parseLines(logLines);

        assertThat(entries.size(), equalTo(2));

    }

    @Test
    public void canGroupUpStructuredLogLines() {

        List<String[]> input = asList(
                new String[] { "joe", "1", "1", "a/1.java" },
                new String[] { "joe", "4", "4", "a/1.java" },
                new String[] { "fred", "2", "2", "a/1.java" });

        Map<AuthorFilenameKey, Integer> output = calculator.groupUp(input);

        assertThat(output.get(keyOf("joe", "a/1.java")), equalTo(10));
        assertThat(output.get(keyOf("fred", "a/1.java")), equalTo(4));
    }

    @Test
    public void whatsGoingOnWithMyKeys() {
        assertThat(keyOf("joe", "a/1.java"), equalTo(keyOf("joe", "a/1.java")));
        assertThat(keyOf("joe", "a/1.java"), not(equalTo(keyOf("fred", "b/2.java"))));
    }
}
