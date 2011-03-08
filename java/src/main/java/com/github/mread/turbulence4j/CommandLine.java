package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.git.GitAdapter;

public class CommandLine {

    public static final String OUTPUT_DIRECTORY_NAME = "t4j-output/";

    private final TemplateManager templateManager;
    private final Turbulence4j turbulence4j;

    public static void main(String[] args) {
        if (args.length == 0) {
            new CommandLine(".").execute();
        } else {
            new CommandLine(args[0]).execute();
        }
    }

    public CommandLine(String workingDirectoryPath) {
        this(new TemplateManager(new File(workingDirectoryPath, OUTPUT_DIRECTORY_NAME)),
                new Turbulence4j(new OutputWriter(new File(workingDirectoryPath, OUTPUT_DIRECTORY_NAME)),
                        new GitAdapter(),
                        new File(workingDirectoryPath)));
    }

    CommandLine(TemplateManager templateManager, Turbulence4j turbulence4j) {
        this.templateManager = templateManager;
        this.turbulence4j = turbulence4j;
    }

    public void execute() {
        templateManager.execute();
        turbulence4j.execute();
    }

}
