package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.AnalysisEngine;
import com.github.mread.turbulence4j.analysisapi.AnalysisRepository;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.google.inject.Provider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class Turbulence4jTest
{

    @Mock
    private GitAdapter mockGitAdapter;
    @Mock
    private AnalysisEngine mockAnalysisEngine;
    @Mock
    private AnalysisRepository mockAnalysisRepository;
    @Mock
    private Provider<AnalysisRepository> mockAnalysisRepositoryProvider;
    @Mock
    private JavaFileFinder fileFinder;

    File workingDirectory = new File(".");
    private Turbulence4j t4j;

    @Before
    public void setup()
    {
        t4j = new Turbulence4j(workingDirectory,
                               new File("target/"),
                               mockGitAdapter,
                               mockAnalysisEngine,
                               mockAnalysisRepositoryProvider,
                               "--all");
        when(mockAnalysisRepositoryProvider.get()).thenReturn(mockAnalysisRepository);        
    }

    @Test
    public void callsAnalysis()
    {
        when(mockGitAdapter.isRepo(any(File.class))).thenReturn(true);
        t4j.execute();
        verify(mockAnalysisEngine).runAll();
    }

    @Test(expected = RuntimeException.class)
    public void failsWithInformationIfWorkingDirectoryIsNotInAGitRepository()
    {
        when(mockGitAdapter.isRepo(any(File.class))).thenReturn(false);
        t4j.execute();
    }

    @Test
    public void setsUpWorkingDirectory()
    {
        t4j.initialiseAnalysisEngine();
        verify(mockAnalysisRepository).setWorkingDirectory(workingDirectory);
    }

    @Test
    public void setsUpFileFinder()
    {
        t4j.initialiseAnalysisEngine();
        verify(mockAnalysisRepository).setFileFinder(any(JavaFileFinder.class));
    }

}
