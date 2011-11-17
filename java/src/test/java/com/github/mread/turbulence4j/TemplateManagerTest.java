package com.github.mread.turbulence4j;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

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
        assertThat(outputDirectory.listFiles(), hasItemInArray(new File(outputDirectory, "t4j-ca.html")));
    }
}
