package com.github.mread.turbulence4j.calculators.complexityovertime;

public class CommitTimeWithComplexityAndNcss {

    private final long timestamp;
    private final int complexity;
    private final int ncss;

    public CommitTimeWithComplexityAndNcss(long timestamp, int complexity, int ncss) {
        this.timestamp = timestamp;
        this.complexity = complexity;
        this.ncss = ncss;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getComplexity() {
        return complexity;
    }

    public int getNcss() {
        return ncss;
    }

}
