package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

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
                mockGitAdapter,
                destinationDirectory);
        analysis.setRange("");

        analysis.configure(new File("."), mockJavaFileFinder);
        analysis.run();

        verify(mockJavaFileFinder, atLeastOnce()).findAllJavaFiles();
        verify(mockGitAdapter).getLog(targetDirectory, "");
        assertThat(destinationDirectory.exists(), equalTo(true));
    }
}
