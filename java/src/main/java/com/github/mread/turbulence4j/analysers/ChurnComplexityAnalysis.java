package com.github.mread.turbulence4j.analysers;

import java.io.File;

import com.github.mread.turbulence4j.analysisapi.BaseAnalysis;
import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.Output;
import com.github.mread.turbulence4j.analysisapi.Transformer;
import com.github.mread.turbulence4j.calculators.ChurnCalculator;
import com.github.mread.turbulence4j.calculators.ComplexityCalculator;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;
import com.github.mread.turbulence4j.output.CanWriteOutput;
import com.github.mread.turbulence4j.output.JsonOutputWriter;
import com.github.mread.turbulence4j.output.OutputWriter;
import com.github.mread.turbulence4j.output.RawOutputWriter;

public class ChurnComplexityAnalysis extends BaseAnalysis {

    public ChurnComplexityAnalysis(File targetDirectory,
            JavaFileFinder javaFileFinder,
            GitAdapter gitAdapter,
            File destinationDirectory) {

        super(
                new Calculator[] {
                        new ChurnCalculator(targetDirectory, javaFileFinder, gitAdapter),
                        new ComplexityCalculator(javaFileFinder) },
                new Transformer[] { new OutputWriter(null) },
                new Output[] {
                        new OutputWriter(new CanWriteOutput[] {
                                new RawOutputWriter(destinationDirectory),
                                new JsonOutputWriter(new File(destinationDirectory, "js/")) }) });
    }
}
