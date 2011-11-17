package com.github.mread.turbulence4j;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.AnalysisRepository;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.google.inject.Provider;

public class CommandLine {

    public static final String OUTPUT_DIRECTORY_NAME = "t4j-output/";

    private final TemplateManager templateManager;
    private final Turbulence4j turbulence4j;

    public static void main(String[] args) {
        if (args.length == 0) {
            new CommandLine(".", "--since=\"1 week ago\"").execute();
        } else if (args.length != 2) {
            System.err.println("Must supply target directory and range");
            System.exit(1);
        } else {
            new CommandLine(args[0], args[1]).execute();
        }
    }

    public CommandLine(String workingDirectoryPath, String range) {
        this(new File(workingDirectoryPath), new File(workingDirectoryPath, OUTPUT_DIRECTORY_NAME), range);
    }

    private CommandLine(File workingDirectory, File outputDirectory, String range) {
        this(new TemplateManager(outputDirectory),
                new Turbulence4j(
                        workingDirectory,
                        outputDirectory,
                        new GitAdapter(),
                        createAnalysisRepositoryProvider(),
                        range));
    }

    private static Provider<AnalysisRepository> createAnalysisRepositoryProvider() {
        return new Provider<AnalysisRepository>() {
            @Override
            public AnalysisRepository get() {
                return new AnalysisRepository();
            }
        };
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
