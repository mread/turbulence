package com.github.mread.turbulence4j.git;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

public class GitAdapterTest {

    @Test
    public void tmpDirectoryIsNotARepo() {
        GitAdapter gitAdapter = new GitAdapter();
        assertThat(gitAdapter.isRepo(new File("/temp")), equalTo(false));
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
