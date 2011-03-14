package com.github.mread.turbulence4j.calculators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.mread.turbulence4j.analysisapi.Calculator;
import com.github.mread.turbulence4j.analysisapi.CalculatorResult;
import com.github.mread.turbulence4j.calculators.ChurnByAuthorCalculator.AuthorFilenameKey;
import com.github.mread.turbulence4j.files.JavaFileFinder;
import com.github.mread.turbulence4j.git.GitAdapter;

public class ChurnByAuthorCalculator implements Calculator<Map<AuthorFilenameKey, Integer>> {

    private final File workingDirectory;
    private final JavaFileFinder fileFinder;
    private final GitAdapter gitAdapter;

    public ChurnByAuthorCalculator(File workingDirectory, JavaFileFinder fileFinder, GitAdapter gitAdapter) {
        this.workingDirectory = workingDirectory;
        this.fileFinder = fileFinder;
        this.gitAdapter = gitAdapter;
    }

    @Override
    public ChurnByAuthorCalculatorResult run() {
        List<String> logWithAuthor = gitAdapter.getLogWithAuthor(workingDirectory);
        List<String[]> parsed = parseLines(logWithAuthor);
        Map<AuthorFilenameKey, Integer> results = groupUp(parsed);
        return new ChurnByAuthorCalculatorResult(results);
    }

    List<String[]> parseLines(List<String> logLines) {
        List<String[]> results = new ArrayList<String[]>();
        for (String line : logLines) {
            results.add(line.split("\t"));
        }
        return results;
    }

    Map<AuthorFilenameKey, Integer> groupUp(List<String[]> lines) {
        Map<AuthorFilenameKey, Integer> results = new HashMap<AuthorFilenameKey, Integer>();
        for (String[] line : lines) {
            AuthorFilenameKey key = keyOf(line[0], line[3]);
            int value = (results.get(key) == null ? 0 : results.get(key))
                    + addsPlusDeletes(line[1], line[2]);
            results.put(key, value);
        }
        return results;
    }

    private int addsPlusDeletes(String addsString, String deletesString) {
        try {
            int adds = Integer.parseInt(addsString);
            int deletes = Integer.parseInt(deletesString);
            return adds + deletes;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    static AuthorFilenameKey keyOf(String author, String filename) {
        return new AuthorFilenameKey(author, filename);
    }

    public static class ChurnByAuthorCalculatorResult implements CalculatorResult<Map<AuthorFilenameKey, Integer>> {

        private final Map<AuthorFilenameKey, Integer> results;

        public ChurnByAuthorCalculatorResult(Map<AuthorFilenameKey, Integer> results) {
            this.results = results;
        }

        @Override
        public Map<AuthorFilenameKey, Integer> getResult() {
            return results;
        }

    }

    public static class AuthorFilenameKey {
        private final String author;
        private final String filename;

        public AuthorFilenameKey(String author, String filename) {
            this.author = author;
            this.filename = filename;
        }

        public String getAuthor() {
            return author;
        }

        public String getFilename() {
            return filename;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(author).append(filename)
                    .toString();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(author).append(filename).toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            AuthorFilenameKey other = (AuthorFilenameKey) obj;

            return new EqualsBuilder().append(author, other.author).append(filename, other.filename).isEquals();
        }

    }
}
