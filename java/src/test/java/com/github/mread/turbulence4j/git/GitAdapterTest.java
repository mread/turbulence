package com.github.mread.turbulence4j.git;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class GitAdapterTest {

    @Test
    // this test breaks if we ever move off git :) problem?
    public void shouldRecogniseAGitRepositoryWhenItSeesOne() {
        GitAdapter gitAdapter = new GitAdapter();
        assertThat(gitAdapter.isRepo(new File(".")), is(true));
    }

    @Test
    public void canGetLog() throws IOException {

        GitAdapter gitAdapter = new GitAdapter();
        BufferedReader log = gitAdapter.getLog(new File("."));

        assertThat(log.readLine(), notNullValue());

    }
}
