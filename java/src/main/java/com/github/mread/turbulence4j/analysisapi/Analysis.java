package com.github.mread.turbulence4j.analysisapi;

public interface Analysis {

    void configure();

    void run();

    void setRange(String range);
}
