package com.github.mread.turbulence4j;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class TemplateManager {

    private static final String TEMPLATES_DIR = "/templates";

    private final File outputDirectory;

    public TemplateManager(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void execute() {
        String templatesDirectoryName = this.getClass().getResource(TEMPLATES_DIR).getFile();
        File templatesDirectory = new File(templatesDirectoryName);
        try {
            FileUtils.copyDirectory(templatesDirectory, outputDirectory,
                    filter(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileFilter filter() {
        IOFileFilter htmlFiles = FileFilterUtils.suffixFileFilter(".html");
        IOFileFilter jsFiles = FileFilterUtils.suffixFileFilter(".js");
        IOFileFilter cssFiles = FileFilterUtils.suffixFileFilter(".css");

        return FileFilterUtils.or(htmlFiles, jsFiles, cssFiles, DirectoryFileFilter.DIRECTORY);
    }
}
