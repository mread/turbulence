package com.github.mread.turbulence4j;

import static com.github.mread.turbulence4j.CommandLine.OUTPUT_DIRECTORY;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.mread.calculators.FileValue;
import com.github.mread.turbulence4j.git.GitAdapter;

public class CommandLineTest {

    private static final String TARGET_DIR = "./target";

    private File expectedOutputDirectory;

    @Before
    public void setup() {
        expectedOutputDirectory = new File(TARGET_DIR, OUTPUT_DIRECTORY);
        expectedOutputDirectory.delete();
    }

    @Test
    public void mainMethodShouldProcessArgs() {

        CommandLine.main(new String[] { TARGET_DIR });

        assertThat(expectedOutputDirectory.exists(), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void failsWithInformationIfWorkingDirectoryIsNotInAGitRepository() {

        GitAdapter mockGitAdapter = mock(GitAdapter.class);
        OutputWriter mockOutputWriter = mock(OutputWriter.class);
        when(mockGitAdapter.isRepo(any(File.class))).thenReturn(false);

        CommandLine commandLine = new CommandLine(mockOutputWriter, mockGitAdapter, "/tmp");
        commandLine.execute();

    }

    @Test
    public void measuresTotalComplexityOfAllJavaFilesInCurrentDirectory() {
        CommandLine commandLine = new CommandLine(".");
        commandLine.execute();

        assertThat(commandLine.getTotalComplexity(), greaterThan(1));
    }

    @Test
    public void measuresTotalChurnOfAllJavaFilesInCurrentDirectory() {
        CommandLine commandLine = new CommandLine(".");
        commandLine.execute();

        assertThat(commandLine.getTotalChurn(), greaterThan(1));
    }

    @Test
    public void outputsBasicInfoToAFile() {
        GitAdapter mockGitAdapter = new GitAdapter();
        OutputWriter mockOutputWriter = mock(OutputWriter.class);
        CommandLine commandLine = new CommandLine(mockOutputWriter, mockGitAdapter, ".");
        commandLine.execute();

        verify(mockOutputWriter).write(any(File.class),
                any(File.class),
                anyListOf(FileValue.class),
                anyListOf(FileValue.class));
    }

    @Test
    @Ignore
    public void runAgainstSpecificTarget() {
        CommandLine commandLine = new CommandLine("/work/workspaces/Frame-git2/Services");
        commandLine.execute();
        assertThat(commandLine.getTotalChurn(), greaterThan(1));
    }
}
