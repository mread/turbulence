package com.github.mread.calculators;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.calculators.ChurnCalculator.NameValue;
import com.github.mread.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ChurnCalculatorTest {

    private static final BufferedReader EXAMPLE_RAW_OUTPUT_WITH_NEWLINES =
            new BufferedReader(new StringReader("\n\n\n\n10\t6\tlib/turbulence.java\n\n\n\n17\t2\tlib/eddies.java\n"));

    @Mock
    private GitAdapter mockGitAdapter;
    @Mock
    private JavaFileFinder mockJavaFileFinder;
    private ChurnCalculator churnCalculator;

    @Before
    public void setup() {
        churnCalculator = new ChurnCalculator(
                new File("src/test/java/com/github/mread/javancss/"),
                mockJavaFileFinder,
                mockGitAdapter);
    }

    @Test
    public void canFilterOutNewLines() {
        List<String> lines = churnCalculator.withoutNewlines(EXAMPLE_RAW_OUTPUT_WITH_NEWLINES);
        assertThat(lines.size(), equalTo(2));
        assertThat(lines.get(0), equalTo("10\t6\tlib/turbulence.java"));
        assertThat(lines.get(1), equalTo("17\t2\tlib/eddies.java"));
    }

    @Test
    public void totalsAddAndDeletes() {
        List<String> input = asList("10\t6\tlib/turbulence.java", "17\t2\tlib/eddies.java");
        List<NameValue> output = churnCalculator.fileChurn(input);
        assertThat(output.get(0), equalTo(new NameValue("lib/turbulence.java", 16)));
        assertThat(output.get(1), equalTo(new NameValue("lib/eddies.java", 19)));
    }

    @Test
    public void groupsUpByFileAndSumsChurnExcludingLast() {
        List<NameValue> input = new ArrayList<NameValue>();
        input.add(new NameValue("a.java", 5));
        input.add(new NameValue("b.java", 3));
        input.add(new NameValue("a.java", 4));
        input.add(new NameValue("b.java", 8));
        input.add(new NameValue("b.java", 5));
        input.add(new NameValue("a.java", 2));

        List<NameValue> groupedOutput = churnCalculator.groupUp(input);

        assertThat(groupedOutput.get(0), equalTo(new NameValue("a.java", 9)));
        assertThat(groupedOutput.get(1), equalTo(new NameValue("b.java", 11)));

    }

    @Test
    public void singleChangeShouldReturnZeroChurn() {
        List<NameValue> input = new ArrayList<NameValue>();
        input.add(new NameValue("a.java", 5));

        List<NameValue> groupedOutput = churnCalculator.groupUp(input);

        assertThat(groupedOutput.get(0), equalTo(new NameValue("a.java", 0)));

    }

    @Test
    public void exludesUninterestingFile() {

        when(mockJavaFileFinder.findAllJavaFiles()).thenReturn(asList(new File("./a/a.java")));
        List<NameValue> input = asList(new NameValue("a/a.java", 1), new NameValue("b/b.txt", 1));

        List<NameValue> output = churnCalculator.excludingUninterestingFiles(input);

        assertThat(output.size(), equalTo(1));
        assertThat(output, hasItem(new NameValue("a/a.java", 1)));

    }

    @Test
    public void returnsChurnOnlyForFilesRequested() {

        when(mockGitAdapter.getLog(any(File.class)))
                .thenReturn(
                        new BufferedReader(new StringReader(
                                "1\t2\ta.java\n"
                                        + "1\t2\tb.java\n"
                                        + "1\t2\tc.txt\n")));
        when(mockJavaFileFinder.findAllJavaFiles()).thenReturn(asList(new File("a.java"), new File("b.java")));
        ChurnCalculator calculator = new ChurnCalculator(new File("."), mockJavaFileFinder, mockGitAdapter);

        calculator.calculate();

        assertThat(calculator.getResult().size(), equalTo(2));
        assertThat(calculator.getResult(), not(hasItem(new NameValue("c.txt", 0))));
    }
}
