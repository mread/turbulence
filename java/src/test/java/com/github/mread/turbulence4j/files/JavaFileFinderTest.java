package com.github.mread.turbulence4j.files;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class JavaFileFinderTest {

    private static final File BASE_DIR = new File("src/test/java/com/github/mread/files/");

    @Test
    public void canFindAllJavaFiles() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(BASE_DIR);
        List<String> files = javaFileFinder.findAllJavaFiles();
        assertThat("number of .java in this package should be 1", files.size(), equalTo(1));
        assertThat(files, hasItem("JavaFileFinderTest.java"));
    }

    @Test
    public void testDeepPaths() throws IOException {

        File base = new File("target/java-file-finder-test");
        base.delete();
        File base_a = new File(base, "a");
        base_a.mkdirs();
        File base_a_b = new File(base_a, "b");
        base_a_b.mkdirs();
        File base_0 = new File(base, "0.java");
        base_0.createNewFile();
        File aDir_file1 = new File(base_a, "1.java");
        aDir_file1.createNewFile();
        File base_a_b_2 = new File(base_a_b, "2.java");
        base_a_b_2.createNewFile();

        JavaFileFinder finder = new JavaFileFinder(base);
        List<String> files = finder.findAllJavaFiles();

        assertThat(files.size(), equalTo(3));
        assertThat(files, hasItem("0.java"));
        assertThat(files, hasItem("a/1.java"));
        assertThat(files, hasItem("a/b/2.java"));
    }
}
