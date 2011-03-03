package com.github.mread.turbulence4j.git;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

public class GitAdapterTest {

	@Test
	// test breaks if we ever move off git :)
	public void shouldRecogniseAGitRepositoryWhenItSeesOne() {
		GitAdapter gitAdapter = new GitAdapter();
		assertThat(gitAdapter.isRepo(new File(".")), is(true)); 
	}
}
