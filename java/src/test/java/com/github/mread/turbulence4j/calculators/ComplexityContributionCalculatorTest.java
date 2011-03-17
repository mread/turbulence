package com.github.mread.turbulence4j.calculators;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
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
public class ComplexityContributionCalculatorTest {

    @Mock
    private JavaFileFinder mockJavaFileFinder;
    @Mock
    private GitAdapter mockGitAdapter;
    private ComplexityContributionCalculator calculator;

    @Before
    public void setup() {
        calculator = new ComplexityContributionCalculator(new File("."), mockJavaFileFinder, mockGitAdapter);
    }

    @Test
    public void getsAllTheCommits() {
        calculator.getSha1s();
        verify(mockGitAdapter).getLogOfSha1s(new File("."), "");
    }

    @Test
    public void structuresCommits() {
        List<String> input = asList("abc|abc|Matt Read", "def|ghi|Joe Bloggs", "first-commit ");
        List<CommitParentAuthor> results = calculator.structureSha1s(input);
        assertThat(results.size(), equalTo(2));
        assertThat(results.get(0).getCommit(), equalTo("abc"));
        assertThat(results.get(0).getParent(), equalTo("abc"));
        assertThat(results.get(0).getAuthor(), equalTo("Matt Read"));
        assertThat(results.get(1).getCommit(), equalTo("def"));
        assertThat(results.get(1).getParent(), equalTo("ghi"));
        assertThat(results.get(1).getAuthor(), equalTo("Joe Bloggs"));
    }

    @Test
    public void getsAllFilesInACommit() {

        when(mockGitAdapter.getFilesForACommit(any(File.class), eq("d355d")))
                .thenReturn(asList("a/1.java", "b/2.java"));

        List<String> filenames = calculator.filesInACommit("d355d");

        assertThat(filenames.size(), equalTo(2));
        assertThat(filenames.get(0), equalTo("a/1.java"));
        assertThat(filenames.get(1), equalTo("b/2.java"));
    }

    @Test
    public void convertsToSortedList() {
        Map<String, Integer> input = new HashMap<String, Integer>();
        input.put("Joe Blogs", 56);
        input.put("Matt Read", 35);
        List<AuthorValue> sortedList = calculator.convertToSortedList(input);

        assertThat(sortedList.size(), equalTo(2));
        assertThat(sortedList.get(0).getAuthor(), equalTo("Matt Read"));
        assertThat(sortedList.get(0).getValue(), equalTo(35));
    }

    @Test
    public void shouldMeasureDifferenceInComplexityBetweenTwoCommits() throws FileNotFoundException, IOException {

        String targetFile = "java/src/main/java/com/github/mread/turbulence4j/calculators/ChurnCalculator.java";
        String before = "d355d1dd991c825ea72bd91045ebf65c638a13e2";
        String after = "d31cc74b5e1cc7d4939eba0c07011ae8f3fc4c52";

        ComplexityContributionCalculator complexityDifference = new ComplexityContributionCalculator(new File("."),
                mockJavaFileFinder, mockGitAdapter);

        int beforeComplexity = complexityDifference.measureComplexity(before, targetFile).getValue();
        int afterComplexity = complexityDifference.measureComplexity(after, targetFile).getValue();

    }

}
