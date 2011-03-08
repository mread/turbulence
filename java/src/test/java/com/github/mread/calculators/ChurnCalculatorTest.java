package com.github.mread.calculators;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ChurnCalculatorTest {

    private static final File WORKING_DIRECTORY = new File("src/test/java/com/github/mread/javancss/");

    @Mock
    private GitAdapter mockGitAdapter;
    @Mock
    private JavaFileFinder mockJavaFileFinder;
    private ChurnCalculator churnCalculator;

    @Before
    public void setup() {
        churnCalculator = new ChurnCalculator(
                WORKING_DIRECTORY,
                mockJavaFileFinder,
                mockGitAdapter);
    }

    @Test
    public void handlesFileMoves() {
        List<String> input = asList("10\t6\tgithub/mread/{turbulence4j => output}/OutputWriter.java");
        List<FileValue> output = churnCalculator.churnByLogLine(input);
        assertThat(output.get(0), equalTo(fileValueFor("github/mread/output/OutputWriter.java", 0)));
    }

    @Test
    public void parsesMoves() {
        assertThat(churnCalculator.parseForMoves("github/mread/output/OutputWriter.java"),
                equalTo("github/mread/output/OutputWriter.java"));
        assertThat(churnCalculator.parseForMoves("github/mread/{turbulence4j => output}/OutputWriter.java"),
                equalTo("github/mread/output/OutputWriter.java"));
    }

    @Test
    public void totalsAddAndDeletes() {
        List<String> input = asList("10\t6\tlib/turbulence.java", "17\t2\tlib/eddies.java");
        List<FileValue> output = churnCalculator.churnByLogLine(input);
        assertThat(output.get(0), equalTo(fileValueFor("lib/turbulence.java", 16)));
        assertThat(output.get(1), equalTo(fileValueFor("lib/eddies.java", 19)));
    }

    @Test
    public void groupsUpByFileAndSumsChurnExcludingLast() {
        List<FileValue> input = new ArrayList<FileValue>();
        input.add(fileValueFor("a.java", 5));
        input.add(fileValueFor("b.java", 3));
        input.add(fileValueFor("a.java", 4));
        input.add(fileValueFor("b.java", 8));
        input.add(fileValueFor("b.java", 5));
        input.add(fileValueFor("a.java", 2));

        List<FileValue> groupedOutput = churnCalculator.groupUp(input);

        assertThat(groupedOutput.get(0), equalTo(fileValueFor("a.java", 9)));
        assertThat(groupedOutput.get(1), equalTo(fileValueFor("b.java", 11)));

    }

    @Test
    public void singleChangeShouldReturnZeroChurn() {
        List<FileValue> input = new ArrayList<FileValue>();
        input.add(new FileValue(new File("a.java"), 5));

        List<FileValue> groupedOutput = churnCalculator.groupUp(input);

        assertThat(groupedOutput.get(0), equalTo(new FileValue(new File("a.java"), 0)));

    }

    @Test
    public void excludesUninterestingFile() {

        File A_JAVA = new File(WORKING_DIRECTORY, "./a/a.java");
        File B_TXT = new File(WORKING_DIRECTORY, "b/b.txt");
        when(mockJavaFileFinder.findAllJavaFiles()).thenReturn(asList(A_JAVA));
        List<FileValue> input = asList(new FileValue(A_JAVA, 1), new FileValue(B_TXT, 1));

        List<FileValue> output = churnCalculator.excludingUninterestingFiles(input);

        assertThat(output.size(), equalTo(1));
        assertThat(output, hasItem(new FileValue(A_JAVA, 1)));

    }

    @Test
    public void returnsChurnOnlyForFilesRequested() {

        when(mockGitAdapter.getLog(any(File.class)))
                .thenReturn(Arrays.asList("1\t2\ta.java",
                                        "1\t2\tb.java",
                                        "1\t2\tc.txt"));
        when(mockJavaFileFinder.findAllJavaFiles()).thenReturn(asList(
                new File(WORKING_DIRECTORY, "a.java"),
                new File(WORKING_DIRECTORY, "b.java")));

        ChurnCalculator calculator = new ChurnCalculator(WORKING_DIRECTORY, mockJavaFileFinder, mockGitAdapter);
        calculator.calculate();

        assertThat(calculator.getResults().size(), equalTo(2));
        assertThat(calculator.getResults().keySet(), not(hasItem(fileValueFor("c.txt", 0).getFilePath())));
    }

    private FileValue fileValueFor(String child, int value) {
        return new FileValue(new File(WORKING_DIRECTORY, child), value);
    }
}
