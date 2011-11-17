package com.github.mread.turbulence4j.analysisapi;

import java.io.File;

import com.github.mread.turbulence4j.files.JavaFileFinder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisRepositoryTest {

    private static final File A_WORKING_DIRECTORY = new File(".");
    private static final JavaFileFinder A_FILE_FINDER = new JavaFileFinder(A_WORKING_DIRECTORY);
    
    @Mock
    private Analysis mockAnalysis;
    private AnalysisRepository repository;

    @Before
    public void setup() {
        repository = new AnalysisRepository();
    }

    @Test
    public void canRegisterAnAnalysis() {
        repository.register(mockAnalysis);
        assertThat(repository.isRegistered(mockAnalysis), equalTo(true));
    }

    @Test
    public void registeringProvidesCommonParameters() {
        repository.setWorkingDirectory(A_WORKING_DIRECTORY);
        repository.setFileFinder(A_FILE_FINDER);
        repository.register(mockAnalysis);
        verify(mockAnalysis).configure(A_WORKING_DIRECTORY, A_FILE_FINDER);
    }

}
