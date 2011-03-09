package com.github.mread.turbulence4j.calculators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (moves.containsKey(oldName(logLine))) {
                moves.put(oldName(logLine), newName(logLine));
            }
            moves.put(newName(logLine), null);
        } else {
            moves.put(logLine, null);
        }
    }

    private String ignoreTabs(String logLine) {
        if (logLine.contains("\t")) {
            return logLine.substring(logLine.lastIndexOf("\t") + 1);
        }
        return logLine;
    }

    public String getUltimateName(String filename) {
        if (isAMoveLine(filename)) {
            filename = newName(filename);
        }
        if (!moves.containsKey(filename)) {
            throw new RuntimeException("Failed to record: " + filename);
        }
        if (moves.get(filename) == null) {
            return filename;
        }
        return getUltimateName(moves.get(filename));
    }

    private String oldName(String logLine) {
        int firstCurly = logLine.indexOf("{");
        int newPath = logLine.indexOf("=> ", firstCurly) + 3;
        int lastCurly = logLine.indexOf("}", newPath);

        return logLine.substring(0, firstCurly)
                + logLine.substring(firstCurly + 1, newPath - 4)
                + logLine.substring(lastCurly + 1);
    }

    private String newName(String logLine) {
        int firstCurly = logLine.indexOf("{");
        int newPath = logLine.indexOf("=> ", firstCurly) + 3;
        int lastCurly = logLine.indexOf("}", newPath);

        return logLine.substring(0, firstCurly)
                + logLine.substring(newPath, lastCurly)
                + logLine.substring(lastCurly + 1);
    }

    private boolean isAMoveLine(String filename) {
        return filename.contains(MOVE_INDICATOR);
    }
}
