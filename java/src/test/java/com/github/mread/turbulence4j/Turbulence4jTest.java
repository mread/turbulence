package com.github.mread.turbulence4j;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mread.output.OutputWriter;
import com.github.mread.turbulence4j.git.GitAdapter;

@RunWith(MockitoJUnitRunner.class)
public class Turbulence4jTest {

    @Mock
    private GitAdapter mockGitAdapter;

    @Mock
    private OutputWriter mockOutputWriter;

    private Turbulence4j t4j;

    @Before
    public void setup() {
        t4j = new Turbulence4j(mockOutputWriter, mockGitAdapter, new File("."));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void outputsBasicInfoToADirctory() {
        when(mockGitAdapter.isRepo(any(File.class))).thenReturn(true);
        t4j.execute();

        verify(mockOutputWriter).write(anyMap(), anyMap());
    }

    @Test(expected = RuntimeException.class)
    public void failsWithInformationIfWorkingDirectoryIsNotInAGitRepository() {

        when(mockGitAdapter.isRepo(any(File.class))).thenReturn(false);

        t4j.execute();

    }

}
