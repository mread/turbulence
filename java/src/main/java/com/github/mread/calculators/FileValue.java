package com.github.mread.calculators;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class FileValue implements Comparable<FileValue> {

    final String filename;
    int value;

    public FileValue(String filename, int value) {
        this.filename = filename;
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return filename + ": " + value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(filename).append(value).toHashCode();
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

        return new EqualsBuilder()
                .append(filename, other.filename)
                .append(value, other.value)
                .isEquals();
    }

    @Override
    public int compareTo(FileValue o) {
        return this.filename.compareTo(o.filename);
    }

}