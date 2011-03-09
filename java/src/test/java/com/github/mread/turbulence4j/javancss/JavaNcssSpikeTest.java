package com.github.mread.turbulence4j.javancss;

import java.io.File;
import java.util.List;

import javancss.FunctionMetric;
import javancss.Javancss;
import javancss.ObjectMetric;

import org.junit.Test;

public class JavaNcssSpikeTest {

    @Test
    public void canAnalyseWithNcss() {
        Javancss javancss = new Javancss(
                new File("src/test/java/com/github/mread/turbulence4j/javancss/JavaNcssSpikeTest.java"));
        System.out.println(javancss.printJavaNcss());

        // doesn't seem to measure/aggregate CC for a whole file
        List<?> objectMetrics = javancss.getObjectMetrics();
        for (Object object : objectMetrics) {
            ObjectMetric objectMetric = (ObjectMetric) object;
            System.out.println(objectMetric.name + ": " + objectMetric.ccn);
        }

        // does measure CC per method
        List<?> functionMetrics = javancss.getFunctionMetrics();
        for (Object object : functionMetrics) {
            FunctionMetric functionMetric = (FunctionMetric) object;
            System.out.println(functionMetric.name + ": " + functionMetric.ccn);
        }
    }

}
