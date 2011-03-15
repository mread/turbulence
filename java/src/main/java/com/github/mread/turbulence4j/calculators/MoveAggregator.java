package com.github.mread.turbulence4j.calculators;

import static com.github.mread.turbulence4j.calculators.MoveUtils.extractNewNameFromLogLine;
import static com.github.mread.turbulence4j.calculators.MoveUtils.extractOldNameFromLogLine;
import static com.github.mread.turbulence4j.calculators.MoveUtils.isAMoveLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveAggregator {

    static final String MOVE_INDICATOR = " => ";
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
        if (moves.containsKey(extractOldNameFromLogLine(logLine))) {
            moves.put(extractOldNameFromLogLine(logLine), extractNewNameFromLogLine(logLine));
        }
        moves.put(extractNewNameFromLogLine(logLine), null);
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
            filename = extractNewNameFromLogLine(filename);
        }
        if (!moves.containsKey(filename)) {
            throw new RuntimeException("Failed to record: " + filename);
        }
        if (moves.get(filename) == null) {
            return filename;
        }
        return getRecursiveUltimateName(moves.get(filename), depth++);
    }
}
