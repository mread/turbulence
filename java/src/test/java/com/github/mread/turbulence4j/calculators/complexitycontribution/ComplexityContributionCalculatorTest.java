package com.github.mread.turbulence4j.calculators.complexitycontribution;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.turbulence4j.calculators.CommitParentAuthor;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ComplexityContributionCalculatorTest {

    @Mock
    private GitAdapter mockGitAdapter;
    @Mock
    private JavaFileFinder mockJavaFileFinder;
    private ComplexityContributionCalculator calculator;

    @Before
    public void setup() {
        File targetDirectory = new File(".");
        calculator = new ComplexityContributionCalculator(targetDirectory, mockGitAdapter, new ComplexityContributionProcessor(mockGitAdapter, targetDirectory, mockJavaFileFinder));
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

}
