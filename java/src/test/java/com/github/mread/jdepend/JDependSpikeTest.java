package com.github.mread.jdepend;

import static org.junit.Assert.fail;

import java.util.Collection;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;

import org.junit.Test;

public class JDependSpikeTest {

    @Test
    public void canAnalyseAFile() {
        JDepend jDepend = new JDepend();
        jDepend.addPackage(new JavaPackage("com.github.mread.jdepend"));
        Collection<?> analysis = jDepend.analyze();
        for (Object object : analysis) {
            JavaPackage pack = (JavaPackage) object;
            if (pack.containsCycle()) {
                System.out.println("Found cycle: " + pack.getName());
                fail("Found cycle");
            }
            // turns out jdepend doesn't measure complexity - good spike :)
        }
    }
}
