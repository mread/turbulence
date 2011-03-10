package com.github.mread.turbulence4j.analysers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

@RunWith(MockitoJUnitRunner.class)
public class ChurnComplexityAnalysisTest {

    @Mock
    private JavaFileFinder mockJavaFileFinder;
    @Mock
    private GitAdapter mockGitAdapter;

    @Test
    public void interactsSanely() {
        File targetDirectory = new File(".");
        File destinationDirectory = new File("target/churn-complexity-analysis-test/");
        destinationDirectory.delete();
        ChurnComplexityAnalysis analysis = new ChurnComplexityAnalysis(
                targetDirectory,
                mockJavaFileFinder,
                mockGitAdapter,
                destinationDirectory);

        analysis.run();

        verify(mockJavaFileFinder, atLeastOnce()).findAllJavaFiles();
        verify(mockGitAdapter).getLog(targetDirectory);
        assertThat(destinationDirectory.exists(), equalTo(true));
    }
}
