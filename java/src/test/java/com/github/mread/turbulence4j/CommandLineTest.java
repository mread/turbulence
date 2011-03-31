package com.github.mread.turbulence4j;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandLineTest {

    private static final String EXAMPLE_TARGET_DIR = "./target/command-line-test";

    @Mock
    private Turbulence4j mockTurbulence4j;

    @Mock
    private TemplateManager mockTemplateManager;

    @Test
    @Ignore("takes too long and duplicated by the specific target test below")
    public void mainMethodShouldProcessArgs() {
        File expectedOutputDirectory = new File(EXAMPLE_TARGET_DIR, CommandLine.OUTPUT_DIRECTORY_NAME);
        expectedOutputDirectory.delete();

        CommandLine.main(new String[] { EXAMPLE_TARGET_DIR });

        assertThat(expectedOutputDirectory.exists(), is(true));
    }

    @Test
    public void callsTemplateManager() {
        CommandLine commandLine = new CommandLine(mockTemplateManager, mockTurbulence4j);
        commandLine.execute();

        verify(mockTemplateManager).execute();
    }

    @Test
    public void callsTurbulence4j() {
        CommandLine commandLine = new CommandLine(mockTemplateManager, mockTurbulence4j);
        commandLine.execute();

        verify(mockTurbulence4j).execute();
    }

    @Test
    public void runAgainstSpecificTarget() {
        CommandLine.main(new String[] { "/work/workspaces/Frame-git4/Frame", "" });
        //        CommandLine.main(new String[] { "." });
    }
}
