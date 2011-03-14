package com.github.mread.turbulence4j.calculators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

public class MoveAggregator {

    private static final String MOVE_INDICATOR = " => ";
    private final Map<String, String> moves = new HashMap<String, String>();

    public MoveAggregator(List<String> gitLogLines) {
        for (int i = gitLogLines.size() - 1; i >= 0; i--) {
            String logLine = gitLogLines.get(i);
            processLogLine(logLine);
        }
    }

    private void processLogLine(String logLine) {
        logLine = ignoreTabs(logLine);
        if (isAMoveLine(logLine)) {
            handleMoveLine(logLine);
        } else {
            moves.put(logLine, null);
        }
    }

    private void handleMoveLine(String logLine) {
        if (moves.containsKey(oldName(logLine))) {
            moves.put(oldName(logLine), newName(logLine));
        }
        moves.put(newName(logLine), null);
    }

    private String ignoreTabs(String logLine) {
        if (logLine.contains("\t")) {
            return logLine.substring(logLine.lastIndexOf("\t") + 1);
        }
        return logLine;
    }

    public String getUltimateName(String filename) {
        return getRecursiveUltimateName(filename, 0);
    }

    private String getRecursiveUltimateName(String filename, int depth) {
        if (depth > 500) {
            throw new RuntimeException("Detected infinite loop for: " + filename);
        }
        if (isAMoveLine(filename)) {
            filename = newName(filename);
        }
        if (!moves.containsKey(filename)) {
            throw new RuntimeException("Failed to record: " + filename);
        }
        if (moves.get(filename) == null) {
            return filename;
        }
        return getRecursiveUltimateName(moves.get(filename), depth++);
    }

    static String oldName(String logLine) {
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

    static String newName(String logLine) {
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

    private boolean isAMoveLine(String filename) {
        return filename.contains(MOVE_INDICATOR);
    }
}
