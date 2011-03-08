package com.github.mread.turbulence4j;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class TemplateManagerTest {

    private final File outputDirectory = new File("target/template-manager-test");

    @Before
    public void setup() {
        outputDirectory.delete();
    }

    @Test
    public void copiesTemplatesToOutputDirectory() {

        TemplateManager templateManager = new TemplateManager(outputDirectory);
        templateManager.execute();

        assertThat(outputDirectory.exists(), equalTo(true));
        assertThat(outputDirectory.listFiles(), hasItemInArray(new File(outputDirectory, "t4j.html")));
    }
}
