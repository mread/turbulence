package com.github.mread.calculators;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class FileValue implements Comparable<FileValue> {

    final File file;
    int value;

    public FileValue(File file, int value) {
        this.file = file;
        this.value = value;
    }

    public String getFilePath() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getFilePath() + ": " + value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(file).append(value).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        FileValue other = (FileValue) obj;

        return new EqualsBuilder().append(getFilePath(), other.getFilePath()).isEquals();

    }

    @Override
    public int compareTo(FileValue o) {
        return this.file.compareTo(o.file);
    }
}