package com.github.mread.turbulence4j;

import static com.github.mread.turbulence4j.CommandLine.OUTPUT_DIRECTORY;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

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
    public void shouldCreateOutputInSpecifiedWorkingDirectory() throws IOException {

        CommandLine commandLine = new CommandLine(TARGET_DIR);
        commandLine.execute();

        File outputDirectory = commandLine.getOutputDirectory();
        assertThat(outputDirectory.exists(), is(true));
        assertThat(outputDirectory.getCanonicalPath(), equalTo(expectedOutputDirectory.getCanonicalPath()));
    }

    @Test
    public void mainMethodShouldProcessArgs() {

        CommandLine.main(new String[] { TARGET_DIR });

        assertThat(expectedOutputDirectory.exists(), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void failsWithInformationIfWorkingDirectoryIsNotInAGitRepository() {

        GitAdapter mockGitAdapter = mock(GitAdapter.class);
        when(mockGitAdapter.isRepo(any(File.class))).thenReturn(false);

        CommandLine commandLine = new CommandLine(mockGitAdapter, "/tmp");
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
    public void outputsRawTotalComplexityToAFile() {
        CommandLine commandLine = new CommandLine(".");
        commandLine.execute();

        assertThat(new File(expectedOutputDirectory, CommandLine.RAW_OUTPUT_TXT).exists(), is(true));
    }
}
