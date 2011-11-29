package com.github.mread.turbulence4j.git;

import java.io.File;
import java.util.List;


import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import static java.util.Arrays.asList;

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
    public void thisDirectoryProducesLog() {
        GitAdapter gitAdapter = new GitAdapter();
        assertThat(gitAdapter.getLog(new File("."), "").size(), greaterThan(1));
    }

    @Test
    public void parsesCommits() {
        GitAdapter gitAdapter = new GitAdapter();
        List<String> input = asList("abc|abc|Matt Read|1321544378", "def|ghi|Joe Bloggs|1321544376", "first-commit ");
        List<CommitParentAuthorTimestamp> results = gitAdapter.parseSha1s(input);
        assertThat(results.size(), equalTo(2));
        assertThat(results.get(0).getCommit(), equalTo("abc"));
        assertThat(results.get(0).getParent(), equalTo("abc"));
        assertThat(results.get(0).getAuthor(), equalTo("Matt Read"));
        assertThat(results.get(0).getTimestamp(), equalTo(1321544378L));
        assertThat(results.get(1).getCommit(), equalTo("def"));
        assertThat(results.get(1).getParent(), equalTo("ghi"));
        assertThat(results.get(1).getAuthor(), equalTo("Joe Bloggs"));
        assertThat(results.get(1).getTimestamp(), equalTo(1321544376L));
    }

}
