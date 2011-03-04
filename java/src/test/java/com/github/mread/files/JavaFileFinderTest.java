package com.github.mread.files;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.github.mread.files.JavaFileFinder;

public class JavaFileFinderTest {

    @Test
    public void canFindAllJavaFiles() {
        JavaFileFinder javaFileFinder = new JavaFileFinder(new File("src/test/java/com/github/mread/javancss/"));
        List<File> files = javaFileFinder.findAllJavaFiles();
        assertThat("number of .java in this package should be 1", files.size(), equalTo(1));
    }

}
