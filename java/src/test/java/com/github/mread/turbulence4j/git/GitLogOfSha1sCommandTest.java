package com.github.mread.turbulence4j.git;

import java.io.File;

import org.junit.Test;

public class GitLogOfSha1sCommandTest {

    @Test
    public void run() {
        new GitLogOfSha1sCommand(new File("."), "").call();
    }
}
