package com.github.mread.turbulence4j.calculators.complexitycontribution;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mread.turbulence4j.calculators.AuthorValue;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;

@RunWith(MockitoJUnitRunner.class)
public class ComplexityContributionProcessorTest {

    @Mock
    private GitAdapter mockGitAdapter;
    @Mock
    private JavaFileFinder mockJavaFileFinder;
    private ComplexityContributionProcessor processor;

    @Before
    public void setup() {
        processor = new ComplexityContributionProcessor(mockGitAdapter, new File("."), mockJavaFileFinder);
    }
    
    @Test
    public void shouldMeasureDifferenceInComplexityBetweenTwoCommits() {

        String targetFile = "java/src/main/java/com/github/mread/turbulence4j/calculators/ChurnCalculator.java";
        String before = "d355d1dd991c825ea72bd91045ebf65c638a13e2";
        String after = "d31cc74b5e1cc7d4939eba0c07011ae8f3fc4c52";

        int beforeComplexity = processor.measureComplexity(before, targetFile).getValue();
        int afterComplexity = processor.measureComplexity(after, targetFile).getValue();

    }

    @Test
    public void getsAllFilesInACommit() {

        when(mockGitAdapter.getFilesForACommit(any(File.class), eq("d355d")))
                .thenReturn(asList("a/1.java", "b/2.java"));

        List<String> filenames = processor.filesInACommit("d355d");

        assertThat(filenames.size(), equalTo(2));
        assertThat(filenames.get(0), equalTo("a/1.java"));
        assertThat(filenames.get(1), equalTo("b/2.java"));
    }

    @Test
    public void convertsToSortedList() {
        Map<String, Long> input = new HashMap<String, Long>();
        input.put("Joe Blogs", 56L);
        input.put("Matt Read", 35L);
        List<AuthorValue> sortedList = processor.doSort(input);

        assertThat(sortedList.size(), equalTo(2));
        assertThat(sortedList.get(0).getAuthor(), equalTo("Matt Read"));
        assertThat(sortedList.get(0).getValue(), equalTo(35L));
    }

}
