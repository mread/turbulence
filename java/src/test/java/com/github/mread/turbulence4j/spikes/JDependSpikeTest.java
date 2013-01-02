package com.github.mread.turbulence4j.spikes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;

public class JDependSpikeTest {

    private static final String TARGET_DIRECTORY = "/home/readm/work/git-checkouts/dev/eclipse/classes-java";

    @Test
    public void canAnalyseAFile() throws IOException {
        File targetDirectory = new File(TARGET_DIRECTORY);
        assertTrue("target directory doesn't exist: " + targetDirectory.getCanonicalPath(), targetDirectory.exists());
        assertTrue(targetDirectory.isDirectory());
        assertThat("no files in " + targetDirectory.getCanonicalPath(), targetDirectory.list().length, greaterThan(0));
        
        JDepend jDepend = new JDepend();
        jDepend.addDirectory(TARGET_DIRECTORY);
        jDepend.analyze();
        System.out.println("Analysed " + jDepend.getPackages().size() + " packages");
        for (Object object : jDepend.getPackages()) {
            JavaPackage pack = (JavaPackage) object;
            reportCycles(pack);
        }
    }

    private void reportCycles(JavaPackage pack) {
        List<Object> cycles = new ArrayList<Object>();
        if (pack.collectAllCycles(cycles)) {
            System.out.println(pack.getName());
            for (Object cycle : cycles) {
                JavaPackage cyclicPackage = (JavaPackage) cycle;
                System.out.println("    " + cyclicPackage.getName());
            }
        }
    }
}
