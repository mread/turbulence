package com.github.mread.turbulence4j.git;

import java.io.File;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GitAdapterTest {

    @Test
    public void tmpDirectoryIsNotARepo() {
        GitAdapter gitAdapter = new GitAdapter();
        assertThat(gitAdapter.isRepo(new File(System.getProperty("java.io.tmpdir"))), equalTo(false));
    }

    @Test
    public void thisDirectoryIsARepo() {
        GitAdapter gitAdapter = new GitAdapter();
        assertThat(gitAdapter.isRepo(new File(".")), equalTo(true));
    }

    @Test
    public void thisDirectoryProducesLog() throws InterruptedException {
        GitAdapter gitAdapter = new GitAdapter();
        assertThat(gitAdapter.getLog(new File("."), "").size(), greaterThan(1));
    }

}
