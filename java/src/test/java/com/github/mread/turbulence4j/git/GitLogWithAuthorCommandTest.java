package com.github.mread.turbulence4j.git;

import java.io.File;

import org.junit.Test;

public class GitLogWithAuthorCommandTest {

    @Test
    public void run() {
        new GitLogWithAuthorCommand(new File("."), "").call();
    }
}
