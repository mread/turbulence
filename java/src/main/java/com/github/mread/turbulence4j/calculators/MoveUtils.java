package com.github.mread.turbulence4j.calculators;

import org.apache.commons.io.FilenameUtils;

class MoveUtils {

    static String extractOldNameFromLogLine(String logLine) {
        int firstCurly = logLine.indexOf("{");
        int indicator = logLine.indexOf(" => ", firstCurly);
    
        if (firstCurly == -1) {
            return logLine.substring(0, indicator);
        }
        int lastCurly = logLine.indexOf("}", indicator);
        String result = logLine.substring(0, firstCurly)
                + logLine.substring(firstCurly + 1, indicator)
                + logLine.substring(lastCurly + 1);
        return FilenameUtils.normalize(result, true);
    }

    static String extractNewNameFromLogLine(String logLine) {
        int firstCurly = logLine.indexOf("{");
        int newPath = logLine.indexOf("=> ", firstCurly) + 3;
        if (firstCurly == -1) {
            return logLine.substring(newPath);
        }
        int lastCurly = logLine.indexOf("}", newPath);
        return logLine.substring(0, firstCurly)
                + logLine.substring(newPath, lastCurly)
                + logLine.substring(lastCurly + 1);
    }

    static boolean isAMoveLine(String logLine) {
        return logLine.contains(MoveAggregator.MOVE_INDICATOR);
    }

}
