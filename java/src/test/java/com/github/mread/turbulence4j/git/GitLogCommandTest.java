package com.github.mread.turbulence4j.git;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.github.mread.turbulence4j.git.BaseGitCommand.Callback;

public class GitLogCommandTest {

    @Test
    @SuppressWarnings("unchecked")
    public void canFilterOutNewLines() {
        GitLogCommand gitLog = mock(GitLogCommand.class);
        when(gitLog.runGit()).thenReturn(gitLog);
        when(gitLog.getStdOut()).thenReturn(EXAMPLE_RAW_OUTPUT_WITH_NEWLINES);
        BufferedReader mockStdErr = mock(BufferedReader.class);
        when(gitLog.getStdErr()).thenReturn(mockStdErr);
        when(gitLog.withStdOutProcessor(any(Callback.class))).thenCallRealMethod();
        when(gitLog.call()).thenCallRealMethod();

        List<String> lines = gitLog.call();
        assertThat(lines.size(), equalTo(2));
        assertThat(lines.get(0), equalTo("10\t6\tlib/turbulence.java"));
        assertThat(lines.get(1), equalTo("17\t2\tlib/eddies.java"));
    }

    private static final BufferedReader EXAMPLE_RAW_OUTPUT_WITH_NEWLINES =
            new BufferedReader(new StringReader("\n\n\n\n10\t6\tlib/turbulence.java\n\n\n\n17\t2\tlib/eddies.java\n"));

}
