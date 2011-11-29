package com.github.mread.turbulence4j.spikes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;

import org.junit.Test;

public class JDependSpikeTest {

    @Test
    public void canAnalyseAFile() throws IOException {
        JDepend jDepend = new JDepend();
        jDepend.addDirectory("bin/main");
        jDepend.analyze();
        for (Object object : jDepend.getPackages()) {
            JavaPackage pack = (JavaPackage) object;
            System.out.println(pack.getName());
            reportCycles(pack);
        }
    }

    private void reportCycles(JavaPackage pack) {
        List<Object> cycles = new ArrayList<Object>();
        if (pack.collectAllCycles(cycles)) {
            for (Object cycle : cycles) {
                JavaPackage cyclicPackage = (JavaPackage) cycle;
                System.out.println("    " + cyclicPackage.getName());
            }
        }
    }
}
