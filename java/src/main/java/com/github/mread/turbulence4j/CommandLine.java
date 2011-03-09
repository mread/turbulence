package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.output.CanWriteOutput;
import com.github.mread.turbulence4j.output.JsonOutputWriter;
import com.github.mread.turbulence4j.output.OutputWriter;
import com.github.mread.turbulence4j.output.RawOutputWriter;

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
        this(new File(workingDirectoryPath),
                new File(workingDirectoryPath, OUTPUT_DIRECTORY_NAME));
    }

    private CommandLine(File workingDirectory, File outputDirectory) {
        this(new TemplateManager(outputDirectory),
                new Turbulence4j(
                        workingDirectory,
                        new OutputWriter(
                                new CanWriteOutput[] {
                                        new RawOutputWriter(outputDirectory),
                                        new JsonOutputWriter(new File(outputDirectory, "js/"))
                                }),
                        new GitAdapter()));
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
