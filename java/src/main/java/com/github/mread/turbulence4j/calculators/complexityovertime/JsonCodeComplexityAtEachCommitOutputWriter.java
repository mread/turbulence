package com.github.mread.turbulence4j.calculators.complexityovertime;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.Output;

public class JsonCodeComplexityAtEachCommitOutputWriter implements Output {

    private String range;

    public JsonCodeComplexityAtEachCommitOutputWriter(File outputDirectory,
            CodeComplexityAtEachCommitCalculator codeComplexityAtEachCommitCalculator) {
    }

    @Override
    public void output() {

    }

    @Override
    public void setRange(String range) {
        this.range = range;

    }

}
