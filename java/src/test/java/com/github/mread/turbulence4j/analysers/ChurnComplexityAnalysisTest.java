package com.github.mread.turbulence4j.analysers;

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
        ChurnComplexityAnalysis analysis = new ChurnComplexityAnalysis(
                targetDirectory,
                mockJavaFileFinder,
                mockGitAdapter);

        analysis.run();

        verify(mockJavaFileFinder, atLeastOnce()).findAllJavaFiles();
        verify(mockGitAdapter).getLog(targetDirectory);
    }
}
