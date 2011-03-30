package com.github.mread.turbulence4j.calculators;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.turbulence4j.git.GitAdapter;

@RunWith(MockitoJUnitRunner.class)
public class DistinctIssueCommitsPerPackageCalculatorTest {

    @Mock
    private GitAdapter mockGitAdapter;
    private DistinctIssueCommitsPerPackageCalculator calculator;

    @Before
    public void setup() {
        calculator = new DistinctIssueCommitsPerPackageCalculator(mockGitAdapter, new File("."));
    }

    @Test
    public void talksToGitToGetAListOfCommits() {
        calculator.calculate();
        verify(mockGitAdapter).getLogWithCommentsAndDirectories(any(File.class), anyString());
    }

    @Test
    public void parsesSimpleRawLogFormatIntoProcessableFormat() {

        List<String> input = asList("ABC-123: some commit comment",
                       "30.00% src/main/java/a/b/1.java",
                       "70.00% src/main/java/a/b/2.java",
                       "DEF-345: another commit to a different package",
                       "100.00% src/main/java/a/b/c/3.java");

        List<IssuePackage> output = calculator.processRawLog(input);

        assertThat(output, notNullValue());
        assertThat(output.size(), equalTo(3));
        assertThat(output.get(0).getIssueKey(), equalTo("ABC-123"));
        assertThat(output.get(0).getPackageName(), equalTo("a.b"));
        assertThat(output.get(1).getIssueKey(), equalTo("ABC-123"));
        assertThat(output.get(1).getPackageName(), equalTo("a.b"));
        assertThat(output.get(2).getIssueKey(), equalTo("DEF-345"));
        assertThat(output.get(2).getPackageName(), equalTo("a.b.c"));
    }

    @Test
    public void parsesPackageNameFromDirstatOutput() {
        assertThat(
                calculator.extractPackageName("   9.4% java/src/main/java/com/github/mread/turbulence4j/calculators/"),
                equalTo("com.github.mread.turbulence4j.calculators"));
        assertThat(
                calculator.extractPackageName("70.00% src/main/java/a/b/2.java"),
                equalTo("a.b"));
    }

    @Test
    public void recognisesIssueKeyLines() {
        assertThat(calculator.matchesIssueKeyLine(""), equalTo(false));
        assertThat(calculator.matchesIssueKeyLine(" "), equalTo(false));
        assertThat(calculator.matchesIssueKeyLine("ABC-123 no colon"), equalTo(false));
        assertThat(calculator.matchesIssueKeyLine("ABC-A: all letters"), equalTo(false));
        assertThat(calculator.matchesIssueKeyLine("ABC-123: some comment"), equalTo(true));
        assertThat(calculator.matchesIssueKeyLine("DEFG-1: another comment"), equalTo(true));
    }

    @Test
    public void foundIssueKeyWhenNoPreviousIssueFoundShouldCreate() {

        IssuePackage newIssuePackage = calculator.processIssueKeyLine(null, "ABC-123: some comment");

        assertThat(newIssuePackage, notNullValue());
        assertThat(newIssuePackage.getIssueKey(), equalTo("ABC-123"));
    }

    @Test
    public void issueKeyWhenAlreadyHadOneShouldCreateNew() {
        IssuePackage oldIssuePackage = new IssuePackage("ABC-123", "a.b");

        IssuePackage newIssuePackage = calculator.processIssueKeyLine(oldIssuePackage, "DEF-456: some comment");

        assertThat(newIssuePackage, notNullValue());
        assertThat(newIssuePackage.getIssueKey(), equalTo("DEF-456"));
    }

    @Test
    public void recordsTheJiraReferenceForACommitToPackage() {

        List<IssuePackage> input = asList(
                new IssuePackage("ABC-123", "a.b"),
                new IssuePackage("ABC-123", "a.b"),
                new IssuePackage("ABC-123", "a.b.c"),
                new IssuePackage("DEF-123", "a.b"));

        Collection<IssuePackage> output = calculator.issuesForPackageCommits(input);

        assertThat(output, notNullValue());
        assertThat(output.size(), equalTo(3));
    }
}
