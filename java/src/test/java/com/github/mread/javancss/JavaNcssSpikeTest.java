package com.github.mread.javancss;

import java.io.File;
import java.util.List;

import javancss.FunctionMetric;
import javancss.Javancss;
import javancss.ObjectMetric;

import org.junit.Test;

import com.github.mread.files.JavaFileFinder;

public class JavaNcssSpikeTest {

    @Test
    public void canAnalyseWithNcss() {
        Javancss javancss = new Javancss(new JavaFileFinder(new File(".")).findAllJavaFiles());
        System.out.println(javancss.printJavaNcss());

        // doesn't seem to measure/aggregate CC
        List<?> objectMetrics = javancss.getObjectMetrics();
        for (Object object : objectMetrics) {
            ObjectMetric objectMetric = (ObjectMetric) object;
            System.out.println(objectMetric.name + ": " + objectMetric.ccn);
        }

        System.out.println("\n\n\n");

        // does measure CC
        List<?> functionMetrics = javancss.getFunctionMetrics();
        for (Object object : functionMetrics) {
            FunctionMetric functionMetric = (FunctionMetric) object;
            System.out.println(functionMetric.name + ": " + functionMetric.ccn);
        }
    }

}
