package com.github.mread.turbulence4j.analysisapi;

import java.io.File;

import com.github.mread.turbulence4j.files.JavaFileFinder;


public interface Analysis {

    void configure(File workingDirectory, JavaFileFinder fileFinder);

    void run();

    void setRange(String range);
}
